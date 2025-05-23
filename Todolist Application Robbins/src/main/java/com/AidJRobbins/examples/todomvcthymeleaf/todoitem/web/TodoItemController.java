//This is the controller for the application, and contains various get, post, put, and delete mappings to complete various tasks

import com.wimdeblauwe.examples.todomvcthymeleaf.todoitem.TodoItemRepository;

import com.wimdeblauwe.examples.todomvcthymeleaf.todoitem.TodoItem;
import com.wimdeblauwe.examples.todomvcthymeleaf.todoitem.TodoItemNotFoundException;
import com.wimdeblauwe.examples.todomvcthymeleaf.todoitem.TodoItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class TodoItemController {

    private final TodoItemRepository repository;

    public TodoItemController(TodoItemRepository repository) { 
        this.repository = repository;
    }

    @GetMapping
    public String index(Model model) {
        addAttributesForIndex(model, ListFilter.ACTIVE);
        return "index";
    }
    
    @PostMapping
    public String addNewTodoItem(@Valid @ModelAttribute("item") TodoItemFormData formData) { 
        repository.save(new TodoItem(formData.getTitle(), false)); 

        return "redirect:/"; 
    }

    @PutMapping("/{id}/toggle") 
    public String toggleSelection(@PathVariable("id") Long id) { 
        TodoItem todoItem = repository.findById(id) 
                                      .orElseThrow(() -> new TodoItemNotFoundException(id));

        todoItem.setCompleted(!todoItem.isCompleted()); 
        repository.save(todoItem); 
        return "redirect:/"; 
    }
    @DeleteMapping("/{id}") 
    public String deleteTodoItem(@PathVariable("id") Long id) { 
        repository.deleteById(id); 

        return "redirect:/"; 
    }
    @GetMapping
    public String index(Model model) {
        model.addAttribute("item", new TodoItemFormData());
        model.addAttribute("todos", getTodoItems());
        model.addAttribute("totalNumberOfItems", repository.count());
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems()); 
        return "index";
    }
    @GetMapping
    public String index(Model model) {
        addAttributesForIndex(model, ListFilter.ALL);
        return "index";
    }

    @GetMapping("/active")
    public String indexActive(Model model) {
        addAttributesForIndex(model, ListFilter.ACTIVE);
        return "index";
    }

    @GetMapping("/completed")
    public String indexCompleted(Model model) {
        addAttributesForIndex(model, ListFilter.COMPLETED);
        return "index";
    }

    private void addAttributesForIndex(Model model,
                                       ListFilter listFilter) {
        model.addAttribute("item", new TodoItemFormData());
        model.addAttribute("filter", listFilter); 
        model.addAttribute("todos", getTodoItems(listFilter)); 
        model.addAttribute("totalNumberOfItems", repository.count());
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems());
        model.addAttribute("numberOfCompletedItems", getNumberOfCompletedItems());
    }
    private List<TodoItemDto> getTodoItems(ListFilter filter) {
        return switch (filter) { 
            case ALL -> convertToDto(repository.findAll());
            case ACTIVE -> convertToDto(repository.findAllByCompleted(false));
            case COMPLETED -> convertToDto(repository.findAllByCompleted(true));
        };
    }

    private List<TodoItemDto> convertToDto(List<TodoItem> todoItems) {
        return todoItems
                .stream()
                .map(todoItem -> new TodoItemDto(todoItem.getId(),
                                                 todoItem.getTitle(),
                                                 todoItem.isCompleted()))
                .collect(Collectors.toList());
    }
    private void addAttributesForIndex(Model model,
                                       ListFilter listFilter) {
        model.addAttribute("item", new TodoItemFormData());
        model.addAttribute("filter", listFilter);
        model.addAttribute("todos", getTodoItems(listFilter));
        model.addAttribute("totalNumberOfItems", repository.count());
        model.addAttribute("numberOfActiveItems", getNumberOfActiveItems());
        model.addAttribute("numberOfCompletedItems", getNumberOfCompletedItems()); 
    }

    private int getNumberOfCompletedItems() {
        return repository.countAllByCompleted(true); 
    }
    @DeleteMapping("/completed")
public String deleteCompletedItems() {
    List<TodoItem> items = repository.findAllByCompleted(true);
    for (TodoItem item : items) {
        repository.deleteById(item.getId());
    }
    return "redirect:/";
}
@PutMapping("/toggle-all")
public String toggleAll() {
    List<TodoItem> todoItems = repository.findAll();
    for (TodoItem todoItem : todoItems) {
        todoItem.setCompleted(!todoItem.isCompleted());
        repository.save(todoItem);
    }
    return "redirect:/";
}
     private int getNumberOfActiveItems() {
        return repository.countAllByCompleted(false);
    }

    private int getNumberOfCompletedItems() {
        return repository.countAllByCompleted(true);
    }

    public static record TodoItemDto(long id, String title, boolean completed) {
    }

    public enum ListFilter {
        ALL,
        ACTIVE,
        COMPLETED
    }
}
}
