package vztrack.gls.com.vztrack_user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vztrack.gls.com.vztrack_user.R


class Visitor_Approval_Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.approval_fragment, null) as ViewGroup
        return root
    }

}