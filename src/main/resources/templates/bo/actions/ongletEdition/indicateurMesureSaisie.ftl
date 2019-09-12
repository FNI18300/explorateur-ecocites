<#ftl outputFormat="HTML">
<#--Explorateur Écocités-->
<#--Copyright (C) 2019 l'État, ministère chargé du logement-->
<#--This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.-->
<#--This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.-->
<#--You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.-->

<#-- @ftlvariable name="chartJsOptionsJson" type="java.lang.String" -->
<#-- @ftlvariable name="isAdmin" type="java.lang.Boolean" -->
<#-- @ftlvariable name="assoIndicateurAction" type="com.efficacity.explorateurecocites.beans.biz.AssoIndicateurObjetBean" -->
<#-- @ftlvariable name="listMesure" type="java.util.List<com.efficacity.explorateurecocites.beans.biz.MesureIndicateur>" -->
<#-- @ftlvariable name="listCible" type="java.util.List<com.efficacity.explorateurecocites.beans.biz.CibleIndicateur>" -->

<div class="title-container">
    <div class="title">
    ${assoIndicateurAction.indicateur.nom}
    </div>
</div>
<div class="paper m-2 w-100">
    <h4 class="title p-b-10">
        Mesures
    </h4>
    <table class="table with-border">
        <thead>
        <tr>
            <th class="text-center" style="width: 23%">Date</th>
            <th class="text-center" style="width: 23%">Valeur</th>
            <th class="text-center" style="width: 23%">Unité</th>
            <th class="text-center" style="width: 23%">Etat de vérification</th>
            <#if canValide?? && canValide><th style="width: 8%"></th></#if>
        </tr>
        </thead>
        <tbody>
        <#if (listMesure?? && listMesure?size >0)>
            <#list listMesure as object>
                <#assign type="mesure">
                <#assign class="MesureIndicateur">
                <#include "indicateurMesureInfo.ftl">
            </#list>
        </#if>
        <tr class="templateAjoutmesure" style="display: none;">
            <td style="display: inline-flex;">
                <span class="m-t-5 m-r-5">
                    <i class="fa fa-info-circle" data-toggle="tooltip" data-placement="top" title="Renseigner la date de la mesure, et non la date de saisie (aujourd'hui)"></i>
                </span>
                <input placeholder="Date de mesure" type="text" id="datemesureAjout" name="dateMesureAjout"
                       class="form-control datepicker">
            </td>
            <td>
                <#assign typeMesure = assoIndicateurAction.indicateur.typeMesure>
                <#assign idInput="valuemesureAjout">
                <#include "../../components/nouvelleMesureIndicateur.ftl">
            </td>
            <td class="text-center ft-s-11">${assoIndicateurAction.unite}</td>
            <td></td>
            <#if canValide?? && canValide>
                <td></td></#if>
        </tr>
        </tbody>
    </table>
    <div class="row m-t-10">
        <div class="col-12">
            <p id="erreurmesure" class="text-danger d-none"></p>
        </div>
        <div class="col-lg-8 col-12">
            <label for="commentaireMesure">Commentaires (dont sources des données et périmètre de calcul) <i
                    class="fa fa-info-circle" data-toggle="tooltip" data-placement="top"
                    title="Vous pouvez si besoin apporter des précisions sur vos mesures ici (sources des données utilisées, méthode de calcul, périmètre…)"></i></label>
            <textarea <#if !(canEdit?? && canEdit)> disabled</#if> name="commentaireMesure" class="inputSaveAuto form-control" id="commentaireMesure" rows="3"
                      data-objectid="${assoIndicateurAction.id!?c}"
                      data-objectclass="AssoIndicateurObjet">${assoIndicateurAction.commentaireMesure!}</textarea>
        </div>
        <#if canEdit?? && canEdit>
            <div class="col-lg-4 col-12 showAjoutmesureBouton">
                <a class="btn btn-lg btn-success btn-block ft-s-14 btn-bleu-efficacity" onclick="showAjoutIndicateurMesure('mesure');">Ajouter
                    une mesure</a>
            </div>
        </#if>
        <div class="col-lg-4 col-12 ajoutmesureBouton" style="display: none;">
            <a class="btn btn-lg btn-success btn-block ft-s-14 " onclick="showModalConfirmationAjoutMesure();">Valider</a>
            <a class="btn btn-lg btn-outline-secondary btn-block ft-s-14 " onclick="cancelIndicateurMesure('mesure');">Annuler</a>
        </div>
    </div>
</div>
<div class="paper m-2 w-100">
    <h4 class="title p-b-10">
        Cibles
    </h4>
    <table class="table with-border">
        <thead>
        <tr>
            <th class="text-center" style="width: 23%">Date</th>
            <th class="text-center" style="width: 23%">Valeur</th>
            <th class="text-center" style="width: 23%">Unité</th>
            <th class="text-center" style="width: 23%">Etat de vérification</th>
            <#if canValide?? && canValide>
                <th style="width: 8%"></th></#if>
        </tr>
        </thead>
        <tbody>
        <#if (listCible?? && listCible?size >0)>
            <#list listCible as object>
                <#assign type="cible">
                <#assign class="CibleIndicateur">
                <#include "indicateurMesureInfo.ftl">
            </#list>
        </#if>
        <tr class="templateAjoutcible" style="display: none;">
            <td style="display: inline-flex;">
                <span class="m-t-5 m-r-5">
                    <i class="fa fa-info-circle" data-toggle="tooltip" data-placement="top" title="Renseigner la date cible visée, et non la date de saisie (aujourd'hui)"></i>
                </span>
                    <input placeholder="Date cible" type="text" id="datecibleAjout" name="dateCibleAjout"
                       class="form-control datepicker">
            </td>
            <td>
                <#assign typeMesure = assoIndicateurAction.indicateur.typeMesure>
                <#assign idInput="valuecibleAjout">
                <#include "../../components/nouvelleMesureIndicateur.ftl">
            </td>
            <td class="text-center ft-s-11">${assoIndicateurAction.unite}</td>
            <td></td>
            <#if canValide?? && canValide>
                <td></td></#if>
        </tr>
        </tbody>
    </table>
    <div class="row m-t-10">
        <div class="col-12">
            <p id="erreurcible" class="text-danger d-none"></p>
        </div>
        <div class="col-lg-8 col-12">
            <label for="commentaireCible">Commentaires (dont sources des données et périmètre de calcul) <i
                    class="fa fa-info-circle" data-toggle="tooltip" data-placement="top"
                    title="Vous pouvez si besoin apporter des précisions sur vos cibles ici (sources des données utilisées, méthode de calcul, périmètre…)"></i></label>
            <textarea <#if !(canEdit?? && canEdit)> disabled</#if> name="commentaireCible" class="inputSaveAuto form-control" id="commentaireCible" rows="3"
                      data-objectid="${assoIndicateurAction.id!?c}"
                      data-objectclass="AssoIndicateurObjet">${assoIndicateurAction.commentaireCible!}</textarea>
        </div>
        <#if canEdit?? && canEdit>
            <div class="col-lg-4 col-12 showAjoutcibleBouton">
                <a class="btn btn-lg btn-success btn-block ft-s-14 btn-vert-efficacity" onclick="showAjoutIndicateurMesure('cible');">Ajouter
                    une cible</a>
            </div>
        </#if>
        <div class="col-lg-4 col-12 ajoutcibleBouton" style="display: none;">
            <a class="btn btn-lg btn-success btn-block ft-s-14 " onclick="showModalConfirmationAjoutCible();">Valider</a>
            <a class="btn btn-lg btn-outline-secondary btn-block ft-s-14 " onclick="cancelIndicateurMesure('cible');">Annuler</a>
        </div>
    </div>
</div>
<div id="graphPaperContainer" class="paper m-2 w-100 graphContainer">
    <h4 class="title p-b-10">
        Suivi
    </h4>
    <div class="col-lg-12">
        <div style="position: relative; height: 100%;">
            <canvas id="chartMesureIndicateur"></canvas>
        </div>
    </div>
    <input type="hidden" id="assoIndicateurActionId" value="${assoIndicateurAction.id!?c}">
    <input type="hidden" id="printDecimal" value="${assoIndicateurAction.indicateur.typeMesure}">
</div>
<div class="footer"></div>



