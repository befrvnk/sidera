package dev.befrvnk.core.statemachine

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
abstract class MemorizedStateMachine<S : Any, A : Any>(
    private val stateHolder: StateHolder<S>,
) : FlowReduxStateMachine<S, A>({ stateHolder.getState() }) {
    constructor(initialState: S) : this(InMemoryStateHolder { initialState })

    override val state: Flow<S>
        get() = super.state.onEach { stateHolder.saveState(it) }
}

sealed class StateHolder<S : Any> {
    internal abstract fun getState(): S

    internal abstract fun saveState(s: S)
}

private class InMemoryStateHolder<S : Any>(
    private val initialState: () -> S,
) : StateHolder<S>() {
    private var state: S? = null

    override fun getState(): S {
        return state ?: initialState().also { state = it }
    }

    override fun saveState(s: S) {
        state = s
    }
}
