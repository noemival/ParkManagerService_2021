package it.unibo.webApplicationPms.utility

class StateButtonIn private constructor(){

    var statebuttoncarenter: String? = ""

init{
    statebuttoncarenter="Disabled"
}
        private object GetInstance {
            val INSTANCE = StateButtonIn()
        }

        companion object {
            val instance: StateButtonIn by lazy { GetInstance.INSTANCE }
        }
    }

