package com.example.memestreamproto.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memestreamproto.data.MemeRepository
import com.example.memestreamproto.databinding.FragmentFeedBinding
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var repo: MemeRepository
    private lateinit var adapter: SimpleMemeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        repo = MemeRepository(requireContext())
        adapter = SimpleMemeAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        // Observe local Room data → keeps UI in sync
        lifecycleScope.launch {
            repo.observeAll().collect { list ->
                adapter.submitList(list)
            }
        }

        // 🔄 Refresh from server
        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch {
                try {
                    repo.refreshFromServer(userId = null) // pass Firebase uid if needed
                    Toast.makeText(requireContext(), "Refreshed from server", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ➕ Add an offline-only meme
        binding.btnAddOffline.setOnClickListener {
            lifecycleScope.launch {
                repo.createLocalMeme(
                    userId = "demoUser",
                    title = "Offline Meme",
                    url = "https://placehold.co/300x200",
                    tags = listOf("offline", "demo")
                )
                Toast.makeText(requireContext(), "Added offline meme", Toast.LENGTH_SHORT).show()
            }
        }

        // ⬆️ Push pending local memes to server
        binding.btnPush.setOnClickListener {
            lifecycleScope.launch {
                try {
                    repo.pushPending()
                    Toast.makeText(requireContext(), "Pending memes pushed", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Push failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
