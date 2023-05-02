package entiteti;

import java.time.LocalDate;
import java.util.List;

public sealed interface IRadnik permits Radnik {

    public List<Radnik> rodjendanRadnikaNaDanasnjiDan(List<Radnik> radnici);
}
