package courage.library.authserver.dto;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
public class GenericReponse {

    @NotNull private String message;
    @NotNull private String subject;

}
