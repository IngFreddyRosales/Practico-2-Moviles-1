package com.example.practico2.Objets

class Snake(private val gridSize: Int, private val panelWidth: Int, private val panelHeight: Int) {

    private val body = mutableListOf(Pair(5, 5))
    private var direction = Pair(1, 0)

    //Pair(1, 0) -> derecha
    //Pair(-1, 0) -> izquierda
    //Pair(0, 1) -> abajo
    //Pair(0, -1) -> arriba

    fun getBody(): List<Pair<Int, Int>> {
        return body
    }

    fun move() {
        var newHeadX = body[0].first + direction.first
        var newHeadY = body[0].second + direction.second

        if (newHeadX >= panelWidth / gridSize) {
            newHeadX = 0
        } else if (newHeadX < 0) {
            newHeadX = (panelWidth / gridSize) - 1
        }

        if (newHeadY >= panelHeight / gridSize) {
            newHeadY = 0
        } else if (newHeadY < 0) {
            newHeadY = (panelHeight / gridSize) - 1
        }

        // Añadir la nueva cabeza
        val newHead = Pair(newHeadX, newHeadY)
        body.add(0, newHead) // Añade la nueva cabeza
        body.removeAt(body.size - 1) // Elimina la cola (si no ha crecido)
    }

    fun setDirection(newDirection: Pair<Int, Int>) {
        direction = newDirection
    }


    fun addBloc() {
        val newBloc = Pair(body[0].first + direction.first, body[0].second + direction.second)
        body.add(0, newBloc)
    }

    fun getCurrentDirection(): Pair<Int, Int> {
        return direction
    }

    fun checkSelfCollision(): Boolean {
        val head = body[0]
        return body.subList(1, body.size).contains(head)
    }




}