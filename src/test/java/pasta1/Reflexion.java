package pasta1;

import com.elastic.model.Agenda;

public class Reflexion {

    public static void main(String[] args) {

        Class<Agenda> nameClasse = Agenda.class;
        String agenda = nameClasse.getSimpleName().toLowerCase();
        System.out.println(agenda);

    }
}
