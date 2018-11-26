package aldiouma

import poc.aldiouma.Requestmap
import poc.aldiouma.Role
import poc.aldiouma.User
import poc.aldiouma.UserRole

class BootStrap {

    def init = { servletContext ->
        /* ********************************************************************** */
        // CRÉATION DES UTILISATEURS
        /* ********************************************************************** */
        def aldiouma = new User(username: "aldiouma", password: "aldiouma").save()
        def sadou = new User(username: "sadou", password: "sadou").save()
        def daouda = new User(username: "daouda", password: "daouda").save()
        def bambito = new User(username: "bambito", password: "bambito").save()
        def cheikh = new User(username: "cheikh", password: "cheikh").save()

        /* ********************************************************************** */
        // CRÉATION DES RÔLES
        /* ********************************************************************** */
        def role_dev = new Role(authority: "ROLE_DEVELOPPEUR").save()
        def role_resp_support = new Role(authority: "ROLE_RESP_SUPPORT").save()
        new Role(authority: "ROLE_ADMINSECURITE").save()
        new Role(authority: "ROLE_ADMIN").save()

        /* ********************************************************************** */
        // ASSIGNATION DES RÔLES AUX UTILISATEURS
        /* ********************************************************************** */
        new UserRole(user: aldiouma, role: role_dev).save()
        new UserRole(user: daouda, role: role_dev).save()
        new UserRole(user: bambito, role: role_dev).save()
        new UserRole(user: cheikh, role: role_dev).save()
        new UserRole(user: sadou, role: role_resp_support).save()

        /* ********************************************************************** */
        // ASSIGNATION DES DROITS D'ACCÈS
        /* ********************************************************************** */
        [
                [pattern: '/login', access: ['permitAll']],
                [pattern: '/login.*', access: ['permitAll']],
                [pattern: '/login/*', access: ['permitAll']],
                [pattern: '/logout', access: ['permitAll']],
                [pattern: '/logout.*', access: ['permitAll']],
                [pattern: '/logout/*', access: ['permitAll']],
                [pattern: '/logout/index*//**', access: ['permitAll']],
                [pattern: '/error', access: ['permitAll']],
                [pattern: '/index', access: ['permitAll']],
                [pattern: '/index.gsp', access: ['permitAll']],
                [pattern: '/j_spring_security_check', access: ['permitAll']],
                [pattern: '/assets/**', access: ['permitAll']],
                [pattern: '/**/js/**', access: ['permitAll']],
                [pattern: '/**/css/**', access: ['permitAll']],
                [pattern: '/**/images/**', access: ['permitAll']],
                [pattern: '/**/favicon.*', access: ['permitAll']],
                [pattern: '/', access: ['IS_AUTHENTICATED_FULLY']],
                [pattern: '/**', access: ['IS_AUTHENTICATED_FULLY']],
                [pattern: '/*/**', access: ['IS_AUTHENTICATED_FULLY']]
        ].each {
            new Requestmap(url: it.pattern, configAttribute: it.access.join(",")).save()
        }
    }
    def destroy = {
    }
}
