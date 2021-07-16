package com.mj.board.util.ktx

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * Returns a [Lazy] delegate to create [ViewDataBinding].
 *
 * ```
 * class MyActivity : FragmentActivity() {
 *     private val binding by dataBinding<MyActivityBinding>(R.layout.my_activity)
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         // You can skip Activity.setContentView()
 *
 *         binding.myModel = ..
 *     }
 * }
 * ```
 *
 * This property will automatically apply DataBinding's lifecycleOwner to activity.
 */
fun <DB : ViewDataBinding> Activity.dataBinding(@LayoutRes layoutId: Int): Lazy<DB> =
    ActivityDataBindingLazy(this, layoutId)

/**
 * Returns a [Lazy] delegate to create [ViewDataBinding].
 *
 * ```
 * class MyFragment : Fragment() {
 *     private val binding by dataBinding<MyFragmentBinding>(R.layout.my_fragment)
 *
 *     override fun onCreateView(
 *         inflater: LayoutInflater,
 *         container: ViewGroup?,
 *         savedInstanceState: Bundle?
 *     ): View? = binding.root
 * }
 * ```
 *
 * This property will automatically apply DataBinding's lifecycleOwner to fragment and observe
 * fragment recreation.
 */
fun <DB : ViewDataBinding> Fragment.dataBinding(@LayoutRes layoutId: Int, keepView: Boolean = true): Lazy<DB> =
    FragmentDataBindingLazy(this, layoutId, keepView)

private class ActivityDataBindingLazy<DB : ViewDataBinding>(
    private val activity: Activity,
    @LayoutRes private val layoutId: Int
) : Lazy<DB> {

    private var cached: DB? = null

    override val value: DB
        get() = cached ?: DataBindingUtil
            .setContentView<DB>(activity, layoutId)
            .apply { lifecycleOwner = activity as? LifecycleOwner }
            .also { cached = it }

    override fun isInitialized() = cached != null
}

private class FragmentDataBindingLazy<DB : ViewDataBinding>(
    private val fragment: Fragment,
    @LayoutRes private val layoutId: Int,
    private val keepView: Boolean
) : Lazy<DB>, LifecycleObserver {

    private var cached: DB? = null
        set(value) {
            field = value
            if (!keepView) with(fragment.viewLifecycleOwner.lifecycle) {
                when (value != null) {
                    true -> addObserver(this@FragmentDataBindingLazy)
                    else -> removeObserver(this@FragmentDataBindingLazy)
                }
            }
        }

    override val value: DB
        get() = fragment.run {
            cached ?: DataBindingUtil
                .inflate<DB>(layoutInflater, layoutId, container, false)
                .apply { lifecycleOwner = this@run }
                .also { cached = it }
        }

    override fun isInitialized() = cached != null

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyView() {
        cached = null // For fragment's view recreation
    }

    private val Fragment.container: ViewGroup?
        get() = requireActivity().findViewById(fragment.id) as? ViewGroup
}
