package Resource.Autenticacao;

import Geral.ResponseBuilder;
import Model.ErrorMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

//Defini que a @ValidarTokenAutenticacao que vai utilizar essa classe
@ValidarTokenAutenticacao

//Indica que essa classe vai prover a funcionalidade pra @seguro não o contario
@Provider

//E prioridade de execucao, pois podemos ter outras classe filtro
//que devem ser executas em uma ordem expecifica
@Priority(Priorities.AUTHENTICATION)

public class FiltroAutenticacao implements ContainerRequestFilter {

    public static final String chave = "0@mI\"@F+-\">w?eN<KoxH@UhL#}ct6,qmoj`-2N4k0dmHBho+]pU73tgCqms46R-KlFX8(G66`Ax($bkg8g+E[$*'`^WPs5z@Or[R";

    //Aqui fazemos o override do método filter que tem como parâmetro
    // o ContainerRequestContext que é o objeto que podemos manipular a request
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Response r = ResponseBuilder.getResponse(Response.Status.UNAUTHORIZED, new ErrorMessage("Token de acesso inválido", 401, ""));

        //Verifica se o header AUTHORIZATION existe ou não se existe extrai o token 
        //se não aborta a requisição retornando uma NotAuthorizedException
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(r);
            return;
        }

        //extrai o token do header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        //verificamos se o método é valido ou não
        //se não for valido a requisição é abortada e retorna uma resposta com status 401 UNAUTHORIZED
        //se for valida modificamos o o SecurityContext da request 
        //para que quando usarmos o getUserPrincipal retorne o login do usuario 
        try {

            // método que verifica se o token é valido ou não 
            Claims claims = validaToken(token);

            //Caso não for valido vai retornar um objeto nulo e executar um exception
            if (claims == null) {
                requestContext.abortWith(r);
            }

            //Método que modifica o SecurityContext pra disponibilizar o login do usuario
            modificarRequestContext(requestContext, claims.getId());
        } catch (Exception e) {
            //Caso o token for invalido a requisição é abortada e retorna uma resposta com status 401 UNAUTHORIZED
            requestContext.abortWith(r);
        }
    }

    //Método que modifica o SecurityContext
    private void modificarRequestContext(ContainerRequestContext requestContext, String login) {

        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();

        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return "";
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });

    }

    public static Claims validaToken(String token) {

        try {

            //Verifica a validade e consciência do token, caso exista algo errado, é gerado uma exceção
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(chave))
                    .parseClaimsJws(token).getBody();

            return claims;

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    public static String gerarToken(String login, int expiraEmDias) {
        //Define qual vai ser o algoritmo da assinatura, no caso vai ser o HMAC SHA512
        SignatureAlgorithm algoritimoAssinatura = SignatureAlgorithm.HS512;

        //Define até que data o token é pelo quantidade de dias que foi passo pelo parâmetro expiraEmDias
        Calendar expira = Calendar.getInstance();

        expira.add(Calendar.DAY_OF_MONTH, expiraEmDias);

        //Encoda a frase segredo pra base64 pra ser usada na geração do token 
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(chave);

        SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algoritimoAssinatura.getJcaName());

        //E finalmente utiliza o JWT builder pra gerar o token
        JwtBuilder construtor = Jwts.builder()
                .setIssuedAt(new Date())//Data que o token foi gerado

                .setIssuer(login)//Coloca o login do usuário mais podia qualquer outra informação

                .signWith(algoritimoAssinatura, key)//coloca o algoritmo de assinatura e frase segredo já encodada

                .setExpiration(expira.getTime());// coloca até que data que o token é valido

        return construtor.compact();//Constrói o token retornando ele como uma String
    }
}