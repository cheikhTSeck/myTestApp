package poc.aldiouma

class Projet {

    Long id
    Long version

    String intitule
    String description
    User demandeur
    User validateur

    String statut
    Date initieLe
    Date valideLe

    static mapping = {
        description sqlType: 'text'
    }

    static constraints = {
        statut inList: ["ET", "VA", "RJ"]
        validateur nullable: true
        valideLe nullable: true
    }
}
