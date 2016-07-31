/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.achimid.api;

import br.com.achimid.view.API_Generator_MCD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Lourran
 *
 * API para criar classes CONTROLLER e DAO a partir de classes MODEL, utilizando TAMPLATE gerérico das classes CONTROLLER e DAO. 
 * API to create CONTROLLER and DAO classes from MODEL classes using generic tamplate the CONTROLLER and DAO classes.
 * 
 * Este é um Gerador de Classes. Utilizado para gerar classes com o mesmo
 * Tamplate. Foi criado pois em um de meus projetos, eu precisava ficar criando
 * classes do padrão MVC muito repetitivas. Atravez da pasta informada no
 * argumento MODEL_PATH onde possui a classes do padrão MVC serão criadas
 * classes do seguindo o CONTROLLER_TAMPLATE e DAO_TAMPLATE. Para cada classe
 * dentro do argumento MODEL_PATH, será gerado uma equivalente ao
 * CONTROLLER_TAMPLATE e DAO_TAMPLATE Dentro do CONTROLLER_TAMPLATE e
 * DAO_TAMPLATE sera necessario Substituir a parte especifica, referente a
 * classe MODEL por ':TYPE'
 *
 * Ex: Original
 *
 * import br.com.achimid.model.Venda;
 *
 * public class DaoVenda extends DaoGenerico<Venda> implements
 * InterfaceContador<Venda> {
 *
 * public DaoVenda() { super(new Venda()); }
 *
 * public Venda gravarVenda(Venda obj){ super.gravar(obj); }
 *
 * public Venda recuperaVenda(int id){ super.recupera(id); } }
 *
 * Ex: Tranformado em DAO_TAMPLATE
 *
 * import br.com.achimid.model.:TYPE;
 *
 * public class Dao:TYPE extends DaoGenerico<:TYPE> implements
 * InterfaceContador<:TYPE> {
 *
 * public Dao:TYPE() { super(new :TYPE()); }
 *
 * public :TYPE gravar:TYPE(:TYPE obj){ super.gravar(obj); }
 *
 * public :TYPE recupera:TYPE(int id){ super.recupera(id); } }
 *
 * Não esqueca de Substituir nos Imports Tambem.
 *
 * Por padrão a classe será criada com o nomer Controller + Nome da Classe model
 * EX: Classe MODEL = Venda.java -> Classe DAO = DaoVenda.java -> Classe
 * CONTROLLER = ControllerVenda.java
 *
 * Caso necessitel alterar o padrão de nomenclatura das classes CONTROLLER e
 * DAO, informe os parametros CONTROLLER_NAME_PATTERN e DAO_NAME_PATTERN e
 * substituia no tamplate por :NAME_PATTERN
 *
 * EX:
 *
 * import br.com.achimid.model.:TYPE;
 *
 * public class :NAME_PATTERN:TYPE extends DaoGenerico<:TYPE> implements
 * InterfaceContador<:TYPE> {
 *
 * public :NAME_PATTERN:TYPE() { super(new :TYPE()); }
 *
 * public :TYPE gravar:TYPE(:TYPE obj){ super.gravar(obj); }
 *
 * public :TYPE recupera:TYPE(int id){ super.recupera(id); } }
 *
 * Exemplo de Utilização (CMD Windows): Ex1: API_Generator_MCD.jar
 * "br.com.achimid.model" "ControllerVenda.java" "DaoVenda.java" Ex2:
 * API_Generator_MCD.jar "br.com.achimid.model" "ControllerVenda.java"
 * "DaoVenda.java" "DAO" "CONTROLLER"
 *
 * SAIDA: public class DAOItem extends DaoGenerico<Item> implements
 * InterfaceContador<Item> {
 *
 * public DAOItem() { super(new Item()); }
 *
 * public Item gravarItem(Item obj){ super.gravar(obj); }
 *
 * public Item recuperaItem(int id){ super.recupera(id); } }
 *
 */
public class GeneretorMCD {

    private static String CONTROLLER_TAMPLATE;
    private static String DAO_TAMPLATE;
    private static final File CONTROLLER_PATH = new File("controller");
    private static final File DAO_PATH = new File("dao");
    private static File MODEL_PATH;
    private static String CONTROLLER_NAME_PATTERN = "Controller";
    private static String DAO_NAME_PATTERN = "Dao";

    private static final String CHAVE = ":TYPE";
    private static final String CHAVE2 = ":NAME_PATTERN";

    public static void main(String[] args) {
        if (!lerArgumentos(args)) {
            return;
        }
        criaPastas();

        for (File f : MODEL_PATH.listFiles()) {
            String MODEL_NAME;
            if (f.getName().contains(".")) {
                MODEL_NAME = f.getName().substring(0, f.getName().indexOf("."));
            } else {
                MODEL_NAME = f.getName();
            }

            List<String> listController = getList(CONTROLLER_TAMPLATE);
            List<String> listDao = getList(DAO_TAMPLATE);

            try (PrintWriter writer = new PrintWriter(CONTROLLER_PATH + "/" + CONTROLLER_NAME_PATTERN + f.getName(), "UTF-8")) {
                listController.stream().forEach((s) -> {
                    writer.println(s.replace(CHAVE, MODEL_NAME).replace(CHAVE2, CONTROLLER_NAME_PATTERN));
                });
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.err.println("Não é possivel criar o Arquivo: " + CONTROLLER_PATH + "/" + CONTROLLER_NAME_PATTERN + f.getName());
            }

            try (PrintWriter writer = new PrintWriter(DAO_PATH + "/" + DAO_NAME_PATTERN + f.getName(), "UTF-8")) {
                listDao.stream().forEach((s) -> {
                    writer.println(s.replace(CHAVE, MODEL_NAME).replace(CHAVE2, DAO_NAME_PATTERN));
                });
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                System.err.println("Não é possivel criar o Arquivo: " + DAO_PATH + "/" + DAO_NAME_PATTERN + f.getName());
            }
        }

    }

    private static boolean lerArgumentos(String[] args) {
        try {
            if (args.length == 0) {
                new API_Generator_MCD().setVisible(true);
                return false;
            }

            if (args.length < 3) {
                System.err.println("Parametros incorretos! Ex: API_Generator_MCD.jar MODEL_PATH CONTROLLER_TAMPLATE DAO_TAMPLATE");

            }

            MODEL_PATH = new File(args[0]);
            if (!MODEL_PATH.exists()) {
                System.err.println("Pasta Model informada não existe! " + args[0]);
                System.exit(0);
            }

            CONTROLLER_TAMPLATE = args[1];
            if (!new File(CONTROLLER_TAMPLATE).exists()) {
                System.err.println("Arquivo de Tamplate Model Informado não existe! " + CONTROLLER_TAMPLATE);
                System.exit(0);
            }

            DAO_TAMPLATE = args[2];
            if (!new File(DAO_TAMPLATE).exists()) {
                System.err.println("Arquivo de Tamplate Model Informado não existe! " + DAO_TAMPLATE);
                System.exit(0);
            }

            if (args.length > 3) {
                CONTROLLER_NAME_PATTERN = args[3];
            }

            if (args.length > 4) {
                DAO_NAME_PATTERN = args[4];
            }            
        } catch (Exception e) {
            System.err.println("Parametros incorretos! Ex: API_Generator_MCD.jar MODEL_PATH CONTROLLER_TAMPLATE DAO_TAMPLATE [CONTROLLER_NAME_PATTERN] [DAO_NAME_PATTERN]");
            System.exit(0);
        }
        return true;
    }

    private static List getList(String s) {
        try (Stream<String> stream = Files.lines(Paths.get(s))) {
            return stream.collect(Collectors.toList());
        } catch (IOException e) {
        }
        return null;
    }

    private static void criaPastas() {
        //CONTROLLER_PATH.deleteOnExit();
        CONTROLLER_PATH.mkdir();
        //DAO_PATH.deleteOnExit();
        DAO_PATH.mkdir();
    }
}
