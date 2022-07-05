package ru.netology.nmedia.common.utils

import java.lang.RuntimeException

class ServerConnectionException : RuntimeException ("Server connection error")
class NetworkConnectionException: RuntimeException ("Network connection error")