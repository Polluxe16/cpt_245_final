import java.lang.annotation.Inherited;

@Entity
public class Todoitem {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;

    private boolean completed;

    private int getNumberOfActiveItems() {
        return repository.countAllByCompleted(false);
    }

}
