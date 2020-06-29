package com.juarez.ktbdusuarios.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juarez.ktbdusuarios.R
import com.juarez.ktbdusuarios.models.User
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.apply {
            txt_item_user_name.text =
                "Boleto: ${user.ticketNumber}\nNombre: ${user.name}\nPrimer Apellido: ${user.firstSurname}" +
                        "\nSegundo Apellido: ${user.secondSurname}\nEdad: ${user.age}"

            setOnClickListener {
                onItemClickListener?.let { it(user) }
            }
        }
        holder.itemView.item_user_btn_delete.apply {
            setOnClickListener {
                onItemClickListenerDelete?.let { it(user) }
            }
        }
        holder.itemView.item_user_btn_update.apply {
            setOnClickListener {
                onItemClickListenerUpdate?.let { it(user) }
            }
        }
    }

    private var onItemClickListenerDelete: ((User) -> Unit)? = null
    private var onItemClickListenerUpdate: ((User) -> Unit)? = null
    private var onItemClickListener: ((User) -> Unit)? = null

    fun setOnItemClickListenerUpdate(listener: (User) -> Unit) {
        onItemClickListenerUpdate = listener
    }

    fun setOnItemClickListenerDelete(listener: (User) -> Unit) {
        onItemClickListenerDelete = listener
    }

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}