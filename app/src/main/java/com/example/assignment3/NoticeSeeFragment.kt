package com.example.assignment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.assignment3.databinding.FragmentNoticeSeeBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class NoticeSeeFragment : Fragment() {
    lateinit var binding: FragmentNoticeSeeBinding
    lateinit var url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoticeSeeBinding.inflate(inflater, container ,false)
        binding.webView.loadUrl(url)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.supportZoom()
        return binding.root
    }

    companion object {
        fun newInstance(_url : String) : NoticeSeeFragment{
            val mf = NoticeSeeFragment()
            mf.url = _url
            return mf
        }
    }
}