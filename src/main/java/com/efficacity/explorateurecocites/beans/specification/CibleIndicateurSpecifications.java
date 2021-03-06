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

package com.efficacity.explorateurecocites.beans.specification;

import com.efficacity.explorateurecocites.beans.model.CibleIndicateur;
import com.efficacity.explorateurecocites.beans.model.CibleIndicateur_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CibleIndicateurSpecifications {
    public static Specification<CibleIndicateur> hasIdAssoIndicateur(Long id) {
        return (root, query, cb) -> cb.equal(root.get(CibleIndicateur_.idAssoIndicateurObjet), id);
    }

    public static Specification<CibleIndicateur> hasIdAssoIndicateur(List<Long> ids) {
        return (root, query, cb) -> root.get(CibleIndicateur_.idAssoIndicateurObjet).in(ids);
    }

    public static Specification<CibleIndicateur> isValid() {
        return (root, query, cb) -> cb.equal(root.get(CibleIndicateur_.valide), true);
    }
}
