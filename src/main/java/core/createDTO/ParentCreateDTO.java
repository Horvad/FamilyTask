package core.createDTO;

import lombok.*;

/**
 * Клас предначен для создания пользователем родителей
 */
@AllArgsConstructor
@Setter
@Getter
public class ParentCreateDTO {
    /**
     * Имя родителя
     */
    private String name;
    /**
     * id адреса
     */
    private long idAddress;
}
