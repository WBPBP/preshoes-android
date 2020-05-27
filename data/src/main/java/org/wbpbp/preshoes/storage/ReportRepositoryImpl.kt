/**
 * Copyright (C) 2020 WBPBP <potados99@gmail.com>
 *
 * This file is part of Preshoes (https://github.com/WBPBP).
 *
 * Preshoes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Preshoes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.wbpbp.preshoes.storage

import io.realm.RealmList
import org.wbpbp.preshoes.entity.Report
import org.wbpbp.preshoes.repository.ReportRepository
import timber.log.Timber

class ReportRepositoryImpl() : ReportRepository {
    override fun addNewReport(report: Report) {
        Timber.d("Add new report!")
    }

    override fun getAllReports(): RealmList<Report> {
        TODO("Not yet implemented")
    }

    override fun getReportById(id: Int): Report? {
        TODO("Not yet implemented")
    }
}