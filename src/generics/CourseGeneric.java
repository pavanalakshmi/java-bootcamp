package generics;

import java.util.HashMap;
import java.util.Map;

public class CourseGeneric<S,G> {
    S studentId;
    G grade;
    Map<S, G> map = new HashMap<>();
}
