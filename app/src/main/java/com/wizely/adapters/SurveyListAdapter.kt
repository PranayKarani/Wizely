package com.wizely.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wizely.R
import com.wizely.databinding.XSurveyItemBinding
import com.wizely.entitiies.Survey

class SurveyListAdapter(val context: Context) :
    RecyclerView.Adapter<SurveyListAdapter.SurveyItemViewHolder>() {

    var surveys: List<Survey>? = null
        set(surveys) {
            field = surveys
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyItemViewHolder {

        val binding = DataBindingUtil.inflate<XSurveyItemBinding>(
            LayoutInflater.from(context),
            R.layout.x_survey_item,
            parent,
            false
        )

        return SurveyItemViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return if (surveys == null)
            0
        else
            surveys!!.size
    }

    override fun onBindViewHolder(holder: SurveyItemViewHolder, position: Int) {

        surveys?.let {
            holder.setSurvey(it[position])
        }
    }

    inner class SurveyItemViewHolder(private val b: XSurveyItemBinding) : RecyclerView.ViewHolder(b.root) {

        fun setSurvey(survey: Survey) {
            b.survayName.text = survey.name
            b.root.setOnClickListener{
                val str = "Area :${survey.area}\n${survey.locations.size} location points"
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            }
        }

    }

}