package fr.nodesigner.meaoo.mqtt.androidsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.nodesigner.meaoo.androidsample.R
import fr.nodesigner.meaoo.mqtt.android.model.Coordinate
import fr.nodesigner.meaoo.mqtt.androidsample.entity.Transport
import kotlinx.android.synthetic.main.path_view_item.view.ivTransport
import kotlinx.android.synthetic.main.path_view_item.view.tvTime
import kotlin.math.roundToInt

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
            val iconRes = when (path.transport) {
                Transport.WALK -> R.drawable.ic_walk
                Transport.SUBWAY -> R.drawable.ic_subway
                Transport.BIKE -> R.drawable.ic_bike
                Transport.CAR -> R.drawable.ic_car
            }

            holder.ivTransport.setImageDrawable(context.getDrawable(iconRes))
            holder.tvTime.text = formatTime(path.items.sumByDouble { it.cost })
        }
    }

    private fun formatTime(time: Double): String {
        var minutes = time.roundToInt()
        val hours = minutes / 60
        minutes %= 60

        return "$hours:$minutes"
    }

    data class Path(
        val transport: Transport,
        val items: List<PathItem>
    )

    data class PathItem(
        val coordinate: Coordinate,
        val cost: Double,
        val type: Transport
    )

    inner class ViewHolder(view: View, itemClickListener: (Path) -> Unit) :
        RecyclerView.ViewHolder(view) {

        val ivTransport: ImageView = view.ivTransport
        val tvTime: TextView = view.tvTime

        init {
            view.setOnClickListener {
                itemClickListener.invoke(paths[adapterPosition])
            }
        }
    }
}
