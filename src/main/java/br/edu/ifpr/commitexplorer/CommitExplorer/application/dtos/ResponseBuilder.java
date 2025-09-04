package br.edu.ifpr.commitexplorer.CommitExplorer.application.dtos;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResponseBuilder {


     //2xx
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "Operação realizada com sucesso", data);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(true, message, data);
    }

    public static BaseResponse<Object> success(String message) {
        return new BaseResponse<>(true, message, null);
    }

    //4xx e 5xx
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(false, message, null, Collections.singletonList(message));
    }

    public static <T> BaseResponse<T> error(String message, String errorCode) {
        return new BaseResponse<>(false, message, null, errorCode, Collections.singletonList(message));
    }

    public static <T> BaseResponse<T> error(String message, List<String> errors) {
        return new BaseResponse<>(false, message, null, errors);
    }

    public static <T> BaseResponse<T> error(String message, String errorCode, List<String> errors) {
        return new BaseResponse<>(false, message, null, errorCode, errors);
    }

    public static <T> BaseResponse<T> validationError(List<String> validationErrors) {
        return new BaseResponse<>(false, "Erro de validação", null, "VALIDATION_ERROR", validationErrors);
    }

    public static <T> BaseResponse<T> notFound(String resource) {
        return new BaseResponse<>(false, resource + " não encontrado", null, "NOT_FOUND",
                List.of(resource + " não foi encontrado"));
    }

    public static <T> BaseResponse<T> internalError() {
        return new BaseResponse<>(false, "Erro interno do servidor", null, "INTERNAL_ERROR",
                List.of("Ocorreu um erro interno. Tente novamente mais tarde."));
    }

    public static <T> BaseResponse<T> forbidden() {
        return new BaseResponse<>(false, "Acesso negado", null, "FORBIDDEN",
                List.of("Você não tem permissão para acessar este recurso"));
    }

    public static <T> BaseResponse<T> unauthorized() {
        return new BaseResponse<>(false, "Não autorizado", null, "UNAUTHORIZED",
                List.of("Credenciais inválidas ou token expirado"));
    }
}
