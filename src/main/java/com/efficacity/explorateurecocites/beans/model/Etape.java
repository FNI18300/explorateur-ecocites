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

package com.efficacity.explorateurecocites.beans.model;

import isotope.commons.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Cette classe a été autogénérée. Elle sera écrasée à chaque génération.
 *
 * Date de génération : 23/01/2019
 */
@Entity
@Table(
    name = "exp_etape"
)
public class Etape extends BaseEntity {
  @Column(
      name = "id_objet"
  )
  private Long idObjet;

  @Column(
      name = "code_etape",
      length = 255
  )
  private String codeEtape;

  @Column(
      name = "statut",
      length = 255
  )
  private String statut;

  @Column(
      name = "commentaire",
      length = 2147483647
  )
  private String commentaire;

  @Column(
      name = "type_objet",
      length = 50
  )
  private String typeObjet;

  @Column(
      name = "date_validee"
  )
  private LocalDateTime dateValidee;

  @Column(
      name = "validee_par"
  )
  private Long valideePar;

  public Etape() {
  }

  public Long getIdObjet() {
    return this.idObjet;
  }

  public void setIdObjet(Long idObjet) {
    this.idObjet = idObjet;
  }

  public String getCodeEtape() {
    return this.codeEtape;
  }

  public void setCodeEtape(String codeEtape) {
    this.codeEtape = codeEtape;
  }

  public String getStatut() {
    return this.statut;
  }

  public void setStatut(String statut) {
    this.statut = statut;
  }

  public String getCommentaire() {
    return this.commentaire;
  }

  public void setCommentaire(String commentaire) {
    this.commentaire = commentaire;
  }

  public String getTypeObjet() {
    return this.typeObjet;
  }

  public void setTypeObjet(String typeObjet) {
    this.typeObjet = typeObjet;
  }

  public LocalDateTime getDateValidee() {
    return this.dateValidee;
  }

  public void setDateValidee(LocalDateTime dateValidee) {
    this.dateValidee = dateValidee;
  }

  public Long getValideePar() {
    return this.valideePar;
  }

  public void setValideePar(Long valideePar) {
    this.valideePar = valideePar;
  }
}
