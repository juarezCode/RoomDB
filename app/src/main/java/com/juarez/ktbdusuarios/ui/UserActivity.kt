package com.juarez.ktbdusuarios.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.juarez.ktbdusuarios.R
import com.juarez.ktbdusuarios.adapters.UserAdapter
import com.juarez.ktbdusuarios.db.UserDatabase
import com.juarez.ktbdusuarios.models.User
import com.juarez.ktbdusuarios.repository.UserRepository
import com.juarez.ktbdusuarios.viewmodel.UserViewModel
import com.juarez.ktbdusuarios.viewmodel.UserViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.dialog_user.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class UserActivity : AppCompatActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setupRecyclerView()
        val userRepository = UserRepository(UserDatabase(this))
        val viewModelProviderFactory = UserViewModelProviderFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(UserViewModel::class.java)

        addUser()
        deleteUser()
        updateUser()
        usersObserver()
        var job: Job? = null
        edt_search_user.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchUsers(editable.toString())
                            .observe(this@UserActivity, Observer { users ->
                                userAdapter.differ.submitList(users)
                            })
                    } else {
                        usersObserver()
                    }
                }
            }
        }
    }

    private fun usersObserver() {
        viewModel.getSavedUsers().observe(this, Observer { users ->
            if (users.isEmpty())
                Toast.makeText(this, "no hay usuarios", Toast.LENGTH_LONG).show()

            userAdapter.differ.submitList(users)
        })
    }

    private fun updateUser() {
        userAdapter.setOnItemClickListenerUpdate { user ->
            job = CoroutineScope(IO).launch {
                val userSaved = viewModel.getUser(user.id!!)
                withContext(Dispatchers.Main) {
                    showDialog(
                        userSaved,
                        "Actualizar Usuario",
                        true,
                        "Usuario actualizado exitosamente"
                    )
                }
            }
        }
    }

    private fun deleteUser() {
        userAdapter.setOnItemClickListenerDelete { user ->
            viewModel.deleteUser(user)
            Snackbar.make(constraintLayout, "Usuario eliminado exitosamente", Snackbar.LENGTH_LONG)
                .apply {
                    setAction("Undo") {
                        viewModel.saveUser(user)
                    }
                    show()
                }
        }
    }

    private fun addUser() {
        fab_add_user.setOnClickListener { view ->
            edt_search_user.text.clear()
            showDialog(null, "Agregar Usuario", false, "Usuario guardado exitosamente")
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        recycler_view_user.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@UserActivity)
        }
    }

    private fun showDialog(
        userSaved: User?,
        title: String,
        isUpdate: Boolean,
        successMessage: String
    ) {
        job?.cancel()
        val dialogUser =
            LayoutInflater.from(this).inflate(R.layout.dialog_user, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogUser)
            .setTitle(title)
            .setCancelable(false)

        if (isUpdate) {
            dialogUser.dialog_name.setText(userSaved?.name)
            dialogUser.dialog_first_surname.setText(userSaved?.firstSurname)
            dialogUser.dialog_second_surname.setText(userSaved?.secondSurname)
            dialogUser.dialog_age.setText(userSaved?.age.toString())
            dialogUser.dialog_ticket_number.setText(userSaved?.age.toString())
        }

        val dialog = builder.show()

        dialogUser.dialog_btn_ok.setOnClickListener {


            val name = dialogUser.dialog_name.text.toString()
            val firstSurname = dialogUser.dialog_first_surname.text.toString()
            val secondSurname = dialogUser.dialog_second_surname.text.toString()
            val age = dialogUser.dialog_age.text.toString()
            val ticketNumber = dialogUser.dialog_ticket_number.text.toString()


            if (name.isEmpty() || firstSurname.isEmpty() || secondSurname.isEmpty() || age.isEmpty()) {
                dialogUser.txt_dialog_input_required.visibility = View.VISIBLE
            } else {
                if (!isUpdate) {
                    CoroutineScope(IO).launch {
                        val MyUser = viewModel.getTicketNumber(ticketNumber.toInt())
                        withContext(Dispatchers.Main) {
                            if (MyUser != null) {
                                dialogUser.txt_dialog_input_required.text =
                                    "Boleto ${MyUser.ticketNumber} ya registrado"
                                dialogUser.txt_dialog_input_required.visibility = View.VISIBLE
                            } else {
                                val user =
                                    User(
                                        userSaved?.id,
                                        name,
                                        firstSurname,
                                        secondSurname,
                                        age.toInt(),
                                        ticketNumber.toInt()
                                    )
                                viewModel.saveUser(user)

                                Snackbar.make(
                                    constraintLayout,
                                    successMessage,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                dialog.dismiss()
                            }
                        }
                    }
                }else {
                    val user =
                        User(
                            userSaved?.id,
                            name,
                            firstSurname,
                            secondSurname,
                            age.toInt(),
                            ticketNumber.toInt()
                        )
                    viewModel.saveUser(user)

                    Snackbar.make(
                        constraintLayout,
                        successMessage,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }

            }
        }

        dialogUser.dialog_btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}