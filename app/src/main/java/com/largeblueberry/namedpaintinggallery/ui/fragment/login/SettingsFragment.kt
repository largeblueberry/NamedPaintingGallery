package com.largeblueberry.namedpaintinggallery.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.largeblueberry.namedpaintinggallery.R
import com.largeblueberry.namedpaintinggallery.ui.fragment.login.LoginActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // XML 파일로부터 Preference 초기화
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // 초기화 작업
        setupPreferences()
    }

    private fun setupPreferences() {
        // "로그인/회원가입" Preference 클릭 시 LoginActivity로 전환
        findPreference<Preference>("로그인_회원가입")?.setOnPreferenceClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            true
        }
    }
}