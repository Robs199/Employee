package Prova.IntelliJ.Dipendente.entity;




public enum Qualification {
    TERZAMEDIA("TERZAMEDIA"),
    DIPLOMA("DIPLOMA"),
    LAUREA("LAUREA"),
    MASTER("MASTER"),
    DOTTORATO("DOTTORATO");
    private final String QUALIFICATION;

    Qualification(final String QUALIFICATION) {
        this.QUALIFICATION = QUALIFICATION;
    }

}
