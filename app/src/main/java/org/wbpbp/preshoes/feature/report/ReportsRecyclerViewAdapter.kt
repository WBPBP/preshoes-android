package org.wbpbp.preshoes.feature.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wbpbp.preshoes.databinding.ReportItemBinding
import org.wbpbp.preshoes.entity.Report

class ReportsRecyclerViewAdapter(private val viewModel: ReportsViewModel)
    : RecyclerView.Adapter<ReportsRecyclerViewAdapter.ViewHolder>() {

    private var reports: List<Report> = listOf()

    fun setReports(reports: List<Report>) {
        this.reports = reports

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReportItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports[position]

        holder.bind(report)
    }

    override fun getItemCount() = reports.size

    inner class ViewHolder(private val binding: ReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(report: Report) {
            binding.report = report
            binding.vm = viewModel
        }
    }
}