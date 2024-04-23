package com.example.partyplannerbackend.Repositorio


import com.example.partyplannerbackend.DTO.UsuarioLoginDTO
import com.example.partyplannerbackend.Domain.Entidad
import com.example.partyplannerbackend.Domain.Instalacion
import com.example.partyplannerbackend.Domain.Servicio
import com.example.partyplannerbackend.Domain.Usuario
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Component
open class Repositorio<T : Entidad>() {
    val elementos: MutableList<T> = mutableListOf()
    var siguienteID: Int = 1

    fun incrementadorAsignador(elemento: T) {
        elemento.id = siguienteID++
    }

    fun allInstances() = elementos
    fun allInstancesActivos() = elementos.filter { it.activo }

    fun existeElId(id: Int): Boolean = elementos.any { it.id == id }


    fun create(elemento: T) {
        elemento.validar()
        validarInExistencia(elemento)
        this.incrementadorAsignador(elemento)
        elementos.add(elemento)
    }

    fun delete(elemento: T) {
        elementos.remove(getById(elemento.id))
    }

    fun update(elemento: T) {
        elemento.validar()
        val elementoViejo = getById(elemento.id)
        val index = elementos.indexOf(elementoViejo)
        elementos[index] = elemento
    }

    fun getById(id: Int): T {
        validarId(id)
        return elementos.first { it.id == id }
    }


    fun validarInExistencia(elemento: T) {
        if (existeElId(elemento.id)) throw RuntimeException("No se pudo crear, ID ya se encuentra utilizado")
    }

    fun validarId(id: Int) {
        //    if (!existeElId(id)) throw ("El ID $id no es valido")
    }

    fun clear() {
        elementos.clear()
    }


}

@Repository
open class RepoUser : Repositorio<Usuario>(){

    fun getUserPass(userIdentificador: UsuarioLoginDTO) = elementos.filter { user -> user.accesoUsuario(userIdentificador) }

}

@Repository
open class RepoInstalacion : Repositorio<Instalacion>()

@Repository
open class RepoServicios : Repositorio<Servicio>()