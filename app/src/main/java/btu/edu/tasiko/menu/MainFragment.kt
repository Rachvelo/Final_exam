package btu.edu.tasiko.menu

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import btu.edu.tasiko.R
import btu.edu.tasiko.model.User
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var image: ImageView
    private lateinit var name: MaterialTextView
    private lateinit var email: MaterialTextView
    private val auth = FirebaseAuth.getInstance()
    private val user: FirebaseUser? = auth.currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view)

        if (user != null) {
            FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val model = snapshot.getValue(User::class.java)

                        if (model != null) {
                            name.text = model.name
                            email.text = model.email

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
    }

    private fun initialize(view: View) {
        image = view.findViewById(R.id.profile_image)
        name = view.findViewById(R.id.tvName)
        email = view.findViewById(R.id.tvEmail)
    }
}