package btu.edu.tasiko.menu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import btu.edu.tasiko.R
import btu.edu.tasiko.model.User
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditFragment : Fragment(R.layout.fragment_edit) {
    private lateinit var image: ImageView
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var imageInput: EditText
    private lateinit var saveBtn: Button
    private val auth = FirebaseAuth.getInstance().currentUser
    private lateinit var reference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)

        imageInput.addTextChangedListener {
            val text = imageInput.text.toString()
            if (text.isNotEmpty()) {
                Glide.with(image)
                    .asDrawable()
                    .error(android.R.color.holo_red_light)
                    .placeholder(R.color.teal_700)
                    .load(text)
                    .into(image)
            }
        }

        saveBtn.setOnClickListener { save() }

    }

    private fun initialize(view: View) {
        image = view.findViewById(R.id.profile_image)
        nameInput = view.findViewById(R.id.name_input)
        emailInput = view.findViewById(R.id.email_input)
        imageInput = view.findViewById(R.id.image_url)
        saveBtn = view.findViewById(R.id.btn_save)

        reference = FirebaseDatabase.getInstance().reference.child("Users").child(auth!!.uid)
        load()
    }

    private fun load() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getValue(User::class.java)

                if (model != null) {
                    nameInput.setText(model.name)
                    emailInput.setText(model.email)
                    imageInput.setText(model.photo)

                    Glide.with(image)
                        .asDrawable()
                        .load(model.photo)
                        .placeholder(R.color.teal_700)
                        .error(android.R.color.holo_red_light)
                        .into(image)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.details, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save() {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val imageUrl = imageInput.text.toString()

        if (name.isEmpty() || email.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(requireContext(), "შეავსე ყველა ველი!", Toast.LENGTH_SHORT).show()
            return
        }

        val user = User()
        user.name = name
        user.email = email
        user.photo = imageUrl
        reference.setValue(user).addOnSuccessListener {
            Toast.makeText(requireContext(), "შენახულია.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        }
    }
}