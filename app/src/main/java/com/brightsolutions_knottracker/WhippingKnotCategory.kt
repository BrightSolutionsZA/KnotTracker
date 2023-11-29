package com.brightsolutions_knottracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

@Suppress("RemoveExplicitTypeArguments", "UNUSED_PARAMETER")
class WhippingKnotCategory : AppCompatActivity() {
    private lateinit var recyclerViewWhippingKnots: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerViewList : ArrayList<KnotData>
    private lateinit var knotsMap : MutableMap<String,KnotData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whipping_knot)

        //init db
        databaseReference = FirebaseDatabase.getInstance().reference
        recyclerViewList = arrayListOf<KnotData>()

        // Set up the RecyclerView and its adapter
        // Initialize the RecyclerView
        // ref: https://www.youtube.com/watch?v=VVXKVFyYQdQ&t=3s
        recyclerViewWhippingKnots = findViewById(R.id.recyclerViewWhippingKnots)
        recyclerViewWhippingKnots.layoutManager = LinearLayoutManager(this)
        recyclerViewWhippingKnots.setHasFixedSize(true)

        knotsMap = mutableMapOf<String, KnotData>()
        recyclerViewWhippingKnots.adapter = MyAdapter(this, knotsMap)

        // populates list on startup of page
        fetchData()
    }

    // populates recycler view on activity launch
    private fun fetchData()
    {
        // query for retrieving 6 basic knots
        val query = databaseReference.child("userdata").child("productionUsers").child(User.userName.toString()).child("myKnots").child("Whipping")

        query.addListenerForSingleValueEvent(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot)
            {
                for (entrySnapshot in snapshot.children)
                {
                    val knotData = entrySnapshot.getValue(KnotData::class.java) as KnotData
                    knotsMap[entrySnapshot.key.toString()] = knotData
                }
                recyclerViewWhippingKnots.adapter = MyAdapter(this@WhippingKnotCategory,knotsMap) // add knot map to adapter call
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@WhippingKnotCategory, "Data retrieval cancelled", Toast.LENGTH_SHORT).show()
            }


        })
    }

    fun closeWindow(view: View) {
        finish()
    }
}