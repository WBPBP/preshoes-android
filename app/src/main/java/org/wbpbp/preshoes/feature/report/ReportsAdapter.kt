package org.wbpbp.preshoes.feature.report

import android.view.LayoutInflater
import android.view.ViewGroup
import org.wbpbp.preshoes.common.base.BaseRealmAdapter
import org.wbpbp.preshoes.common.base.BaseViewHolder
import org.wbpbp.preshoes.databinding.ReportItemBinding
import org.wbpbp.preshoes.entity.Report

class ReportsAdapter(private val viewModel: ReportsViewModel)
    : BaseRealmAdapter<Report>() {

    init {
        updateData(viewModel.getReports())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ReportItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val report = getItem(position)

        (holder.binding as? ReportItemBinding)?.let {
            it.report = report
            it.vm = viewModel
        }
    }
}