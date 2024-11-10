import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marquenow.R

class ReservaAdapter(
    private val context: Context,
    private var reservas: MutableList<Reserva>,
    private val onEditClick: (Reserva) -> Unit,
    private val onDeleteClick: (Reserva) -> Unit
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.bind(reserva)
    }

    override fun getItemCount(): Int = reservas.size

    fun updateData(newReservas: List<Reserva>) {
        reservas.clear()
        reservas.addAll(newReservas)
        notifyDataSetChanged()
    }

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvData: TextView = itemView.findViewById(R.id.tvData)
        private val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        private val tvEspecialidade: TextView = itemView.findViewById(R.id.tvEspecialidade)
        private val tvMedico: TextView = itemView.findViewById(R.id.tvMedico)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(reserva: Reserva) {
            tvData.text = reserva.data
            tvHora.text = reserva.hora
            tvEspecialidade.text = reserva.especialidade
            tvMedico.text = reserva.medico

            btnEdit.setOnClickListener { onEditClick(reserva) }
            btnDelete.setOnClickListener { onDeleteClick(reserva) }
        }
    }
}
