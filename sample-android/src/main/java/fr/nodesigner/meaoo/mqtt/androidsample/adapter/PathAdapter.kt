package fr.nodesigner.meaoo.mqtt.androidsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import kotlinx.android.synthetic.main.path_view_item.view.tvPath
import kotlinx.android.synthetic.main.path_view_item.view.tvTime

class PathAdapter(
    private val context: Context,
    private val itemClickListener: (Path) -> Unit
) : RecyclerView.Adapter<PathAdapter.ViewHolder>() {

    var paths: List<Path> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.path_view_item, parent, false)
        return ViewHolder(layout, itemClickListener)
    }

    override fun getItemCount(): Int = paths.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val path = paths[position]

        holder.apply {
            holder.tvPath.text = path.paths.toString()
            holder.tvTime.text = path.costs.sum().toString()
        }
    }

    data class Path(
        val transport: Transport,
        val paths: List<Coordinate>,
        val costs: List<Double>
    )

    inner class ViewHolder(view: View, itemClickListener: (Path) -> Unit) :
        RecyclerView.ViewHolder(view) {

        val tvPath: TextView = view.tvPath
        val tvTime: TextView = view.tvTime

        init {
            view.setOnClickListener {
                itemClickListener.invoke(paths[adapterPosition])
            }
        }
    }
}
