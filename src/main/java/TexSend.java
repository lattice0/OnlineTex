import okhttp3.*;

import java.io.*;

public class TexSend {
    public static void main(String[] args) throws Exception{
        //Lê o arquivo .tex
        BufferedReader in = new BufferedReader(new FileReader("documento.tex"));
        String line;
        String document = "";
        while((line = in.readLine()) != null)
        {
            document+=line;
        }
        in.close();
        System.out.println("enviando documento: "+document);
        //Inicia o cliente HTTPs
        OkHttpClient client = new OkHttpClient();
        //Simula o usuário acessando a primeira página para inserir os dados
        Request simula = new Request.Builder()
                .url("https://tex.mendelu.cz/en/")
                .build();
        Response simulaResponse = client.newCall(simula).execute();
        //Monta o formulário para preencher com o conteúdo do documento .tex,
        //perceba que incluí os botões que ele disponibiliza, selecionados no
        //padrão
        RequestBody formBody = new FormBody.Builder()
                .add("pole", document)
                .add("pdf", "PDF")
                .add("preklad", "latex")
                .add("pruchod", "1")
                .add(".cgifields", "komprim")
                .build();
        //Envia para o site, imitando o usuário ao clicar no botão "PDF" que contém lá.
        Request request = new Request.Builder()
                .url("https://tex.mendelu.cz/en/")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        System.out.print("headers: "+response.headers());//printa a resposta do servidor pra caso dê algum erro
        InputStream inputStream = response.body().byteStream();

        OutputStream outputStream =
                new FileOutputStream(new File("document.pdf"));

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }

        System.out.println("Done!");

    }
}