/*
 * Explorateur Écocités
 * Copyright (C) 2019 l'État, ministère chargé du logement
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.efficacity.explorateurecocites.ui.bo.controllers.ecocite.onglets;

import com.efficacity.explorateurecocites.beans.biz.EcociteBean;
import com.efficacity.explorateurecocites.beans.biz.EtapeBean;
import com.efficacity.explorateurecocites.beans.biz.QuestionQuestionnaireEvaluationBean;
import com.efficacity.explorateurecocites.beans.service.EcociteService;
import com.efficacity.explorateurecocites.beans.service.EtapeService;
import com.efficacity.explorateurecocites.ui.bo.forms.QuestionnaireReponseForm;
import com.efficacity.explorateurecocites.ui.bo.service.MessageSourceService;
import com.efficacity.explorateurecocites.ui.bo.service.QuestionnaireEvaluationService;
import com.efficacity.explorateurecocites.ui.bo.service.RightUserService;
import com.efficacity.explorateurecocites.ui.bo.service.notifications.EmailNotificationService;
import com.efficacity.explorateurecocites.utils.enumeration.*;
import com.efficacity.explorateurecocites.utils.log.LoggingUtils;
import isotope.commons.validation.ValidationErrorBuilder;
import isotope.modules.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;

@Controller
@RequestMapping("/bo/ecocites")
public class EcociteQuestionnaireController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EcociteQuestionnaireController.class);
    @Autowired
    EmailNotificationService emailNotificationService;

    private final EcociteService ecociteService;

    private final EtapeService etapeService;

    private final RightUserService rightUserService;

    private final QuestionnaireEvaluationService questionnaireEvaluationService;

    private final MessageSourceService messageSourceService;

    public EcociteQuestionnaireController(final MessageSourceService messageSourceService, final RightUserService rightUserService,final EcociteService ecociteService, final EtapeService etapeService, final QuestionnaireEvaluationService questionnaireEvaluationService) {
        this.messageSourceService = messageSourceService;
        this.rightUserService = rightUserService;
        this.ecociteService = ecociteService;
        this.etapeService = etapeService;
        this.questionnaireEvaluationService = questionnaireEvaluationService;
    }

    @GetMapping("questionnaire/{ecociteId}/{codeQuestionnaire}")
    public String getActionQuestionnaireView(Model model, @PathVariable Long ecociteId, @PathVariable String codeQuestionnaire) {

        gestionDroit(model, ecociteId);

        if(model.containsAttribute("canConsulte") && !(boolean) model.asMap().get("canConsulte")){
            return "bo/components/errorAccess";
        }

        LinkedHashMap<String, LinkedList<QuestionQuestionnaireEvaluationBean>> questionnaire = questionnaireEvaluationService.genererQuestionnaire(codeQuestionnaire, ecociteId, TYPE_OBJET.ECOCITE.getCode());
        model.addAttribute("objetId", ecociteId);
        model.addAttribute("objetType", TYPE_OBJET.ECOCITE.getCode());
        model.addAttribute("codeQuestionnaire", codeQuestionnaire);
        model.addAttribute("questionnaire", questionnaire);

        return "bo/questionnaire/questionnaire";
    }

    @PostMapping(value="questionnaire/validation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> validationQuestionnaire(QuestionnaireReponseForm questionnaireReponseForm, Model model,
                                                     BindingResult result, Locale locale) {
        JwtUser user = null;
        if (model.containsAttribute("user")) {
            user = ((JwtUser) model.asMap().get("user"));
        }
        // Sur une ecocite la soumission est la validation
        Long ecociteId = Long.valueOf(questionnaireReponseForm.getObjetId());
        gestionDroit(model, ecociteId);
        if(model.containsAttribute("canValide") && (boolean) model.asMap().get("canValide")){
            EcociteBean ecocite = ecociteService.findOneEcocite(Long.valueOf(questionnaireReponseForm.getObjetId()));
            // Validation des champs du questionnaire
            QUESTIONNAIRE_CODE code = QUESTIONNAIRE_CODE.getByCode(questionnaireReponseForm.getCodeQuestionnaire());
            if (code != null) {
                questionnaireEvaluationService.validationQuestionnaire(code, ecociteId, TYPE_OBJET.ECOCITE, result, locale, true);
            }
            // La sauvegarde auto à déjà tout sauvegardé en base on on a pas besoin de le refaire, En FO si y a aucune erreur, bah on va indiqué que la sauvegarde a marché
            if(!result.hasErrors()) {
                // Update du statue de l'étape
                Long idUser = null;
                if (model.containsAttribute("user")){
                    idUser = ((JwtUser) model.asMap().get("user")).getId();
                }
                etapeService.updateStatusEtape(ecocite.getEtapeByStatus(ETAPE_ECOCITE.CONTEXTE_ET_FACTEUR), ETAPE_STATUT.VALIDER, idUser);
                LoggingUtils.logAction(LOGGER,LoggingUtils.ActionType.VALIDATION,LoggingUtils.SecondaryType.QUESTIONNAIRE,ecocite.getTo(),user);
                emailNotificationService.sendNotificationEmailEtapeEcocite(model,ETAPE_ECOCITE.CONTEXTE_ET_FACTEUR,ecocite.getId());
            }
        } else {
            result.reject("error.user.right", messageSourceService.getMessageSource().getMessage("error.user.right", null, locale));
        }
        return ResponseEntity.ok(ValidationErrorBuilder.fromBindingErrors(result));
    }

    @PostMapping(value="questionnaire/annulationValidation", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> annulationValidationQuestionnaire(QuestionnaireReponseForm questionnaireReponseForm, Model model,
                                                               BindingResult result, Locale locale) {
        JwtUser user = null;
        if (model.containsAttribute("user")) {
            user = ((JwtUser) model.asMap().get("user"));
        }
        gestionDroit(model, Long.valueOf(questionnaireReponseForm.getObjetId()));
        if(model.containsAttribute("canEdit") && (boolean) model.asMap().get("canEdit")){
            EcociteBean ecocite = ecociteService.findOneEcocite(Long.valueOf(questionnaireReponseForm.getObjetId()));
            Long idUser = null;
            if (model.containsAttribute("user")){
                idUser = ((JwtUser) model.asMap().get("user")).getId();
            }
            etapeService.updateStatusEtape(ecocite.getEtapeByStatus(ETAPE_ECOCITE.CONTEXTE_ET_FACTEUR), ETAPE_STATUT.A_RENSEIGNER, idUser);
            LoggingUtils.logAction(LOGGER,LoggingUtils.ActionType.ANNULATION,LoggingUtils.SecondaryType.QUESTIONNAIRE,ecocite.getTo(),user);

        } else {
            result.reject("error.user.right", messageSourceService.getMessageSource().getMessage("error.user.right", null, locale));
        }
        return ResponseEntity.ok(ValidationErrorBuilder.fromBindingErrors(result));
    }

    private void gestionDroit(Model model, Long ecociteId){
        EtapeBean etape = etapeService.toEtapeBean(etapeService.getEtapeByEcociteAndCode(ecociteId, ETAPE_ECOCITE.CONTEXTE_ET_FACTEUR.getCode()));
        if(etape!=null) {
            if (ETAPE_STATUT.VALIDER.getCode().equals(etape.getStatut())) {
                model.addAttribute("titrePageValidation", etapeService.getTitrePage(etape));
                model.addAttribute("etapeDisabled", true);
                // On test si il peuvent modifier aprés validation pour afficher le bouton
                model.addAttribute("canEdit",
                        rightUserService.canModifObjet(model, ecociteId, CODE_FUNCTION_PROFILE.MODIF_CONTEXTE_ET_FACTEUR_ECOCITE.getCode(), TYPE_OBJET.ECOCITE.getCode()));
                model.addAttribute("canEditFile",
                        rightUserService.canModifObjet(model, ecociteId, CODE_FUNCTION_PROFILE.EDIT_CONTEXTE_ET_FACTEUR_ECOCITE.getCode(), TYPE_OBJET.ECOCITE.getCode()));
                model.addAttribute("canConsulte", rightUserService.isHisObject(model, ecociteId, TYPE_OBJET.ECOCITE.getCode()));
            } else {
                if (ETAPE_STATUT.ENVOYER.getCode().equals(etape.getStatut())) {
                    // On test si il peuvent valider une étape pour afficher le bouton
                    if (rightUserService.canModifObjet(model, ecociteId, CODE_FUNCTION_PROFILE.VALIDATE_CONTEXTE_ET_FACTEUR_ECOCITE.getCode(), TYPE_OBJET.ECOCITE.getCode())) {
                        model.addAttribute("canValide", true);
                    } else {
                        model.addAttribute("etapeDisabled", true);
                    }
                    model.addAttribute("canConsulte", rightUserService.isHisObject(model, ecociteId, TYPE_OBJET.ECOCITE.getCode()));
                } else {
                    if (rightUserService.canModifObjet(model, ecociteId, CODE_FUNCTION_PROFILE.EDIT_CONTEXTE_ET_FACTEUR_ECOCITE.getCode(), TYPE_OBJET.ECOCITE.getCode())) {
                        model.addAttribute("canValide", true);
                        model.addAttribute("canEditFile", true);
                        model.addAttribute("canConsulte", true);
                    } else {
                        model.addAttribute("etapeDisabled", true);
                        model.addAttribute("canConsulte", rightUserService.isHisObject(model, ecociteId, TYPE_OBJET.ECOCITE.getCode()));
                    }
                }
            }
        }
    }
}
