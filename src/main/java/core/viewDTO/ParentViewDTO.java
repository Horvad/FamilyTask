package core.viewDTO;

import lombok.*;

import java.util.List;

/**
 * Клас предназначен для отображения радителей
 */
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class ParentViewDTO {
    /**
     * id
     */
    private final long id;
    /**
     * текущая версия
     */
    private final long version;
    /**
     * имя
     */
    private String name;
    /**
     * id адреса проживания
     */
    private long addressId;
}
