package com.example.schedultask

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RequirementFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private lateinit var adapter: RequirementAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_requirement_, container, false)
        val recyclerView:RecyclerView=view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        adapter = RequirementAdapter(object : RequirementAdapter.OnItemClickListener {
            override fun onItemClick(item: Requirement) {
                val intent = Intent(requireContext(), RequirementScreen::class.java).apply {
                    val id:Int=item.id
                    putExtra("item_id", id)
                }
                startActivity(intent)
            }
        })
        recyclerView.adapter=adapter
        viewModel.allRequirements.observe(viewLifecycleOwner, Observer { requirements ->
            adapter.setData(requirements)
        })

        val btn: FloatingActionButton =view.findViewById(R.id.fabAdd);
        btn.setOnClickListener{
            val intent: Intent = Intent(activity,RequirementDetails::class.java)
            startActivity(intent)
        }
        return view
    }
}