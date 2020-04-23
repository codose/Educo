package ng.educo.views.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ng.educo.views.main.ui.ReceivedRequestFragment
import ng.educo.views.main.ui.SentRequestFragment

class RequestPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ReceivedRequestFragment()
            1 -> SentRequestFragment()
            else -> null!!
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Received"
            1 -> "Sent"
            else -> null
        }
    }
}
