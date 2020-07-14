package soy.gabimoreno.audioclean.domain.usecase

class GetAudioSessionIdUseCase(
    private val getActiveSessionsUseCase: GetActiveSessionsUseCase
) {

    sealed class Error(val type: Int) {
        object NoActiveSession : Error(-1)
        object NotFoundSessionId : Error(-2)
    }

    operator fun invoke(): Int {
        try {
//            val currentSession = getActiveSessionsUseCase()[0]
            return 801
        } catch (e: Exception) {
            return Error.NoActiveSession.type
        }
        return Error.NotFoundSessionId.type
    }
}
