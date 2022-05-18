package com.example.a12mayvolleyanimalapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.a12mayvolleyanimalapp.databinding.ActivityMainBinding
import com.google.android.material.R.drawable.m3_popupmenu_background_overlay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passData()


        binding.btnNext.setOnClickListener {
            binding.loadingSpinner.visibility = View.VISIBLE
            passData()
        }


    }

    private fun passData() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET, Constants.BASE_URL, { apiResponse: String ->
                val typeToken = object : TypeToken<Animal>() {}
                val gson = Gson()
                try {
                    val response: Animal = gson.fromJson(apiResponse, typeToken.type)
                    binding.apply {
                        animalNameTxt.text = response.name
                        animalTypeTxt.text = response.animal_type
                        animalHabitatTxt.text = response.habitat
                        Glide.with(this@MainActivity).load(response.image_link)
                            .placeholder(m3_popupmenu_background_overlay)
                            .into(binding.animalImage)

                        if (response.name != null){
                            animalNameTxt.visibility = View.VISIBLE
                        }
                        if (response.animal_type != null){
                            animalTypeTxt.visibility = View.VISIBLE
                        }
                        if (response.habitat != null){
                            animalHabitatTxt.visibility = View.VISIBLE
                        }
                        if (response.image_link != null){
                            animalImage.visibility = View.VISIBLE
                        }
                        binding.loadingSpinner.visibility = View.GONE

                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    binding.loadingSpinner.visibility = View.GONE
                }
            },{ error->
                binding.loadingSpinner.visibility = View.GONE
                error.printStackTrace()
                Toast.makeText(this, "Error is $error", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(request)
    }
}