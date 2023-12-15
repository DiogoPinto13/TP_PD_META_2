package isec.pd.meta2.User.UIControllers;

public class Eventos {
    int id;
    String designacao, local, horaInicio, horaFim;

    public void setID(int id) {
        this.id=id;
    }

    public void setDesignacao(String designacao) {
        this.designacao=designacao;
    }
    public String getDesignacao() {
        return designacao;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio=horaInicio;
    }
    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim=horaFim;
    }
    public String getHorafim() {
        return horaFim;
    }

    public void setLocal(String local){
        this.local=local;
    }
    public String getLocal(){
        return local;
    }
}
