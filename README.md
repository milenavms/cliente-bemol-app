# Projeto Android 

### Overview
 
 - Esta aplicação apresenta a mesmas funcionalidades básicas que frontend web desenvolvido. Se 
 Comunica com a mesma API.
 
### Funcionalidade
 - Cadastro de cliente
 - Login de cliente 
 - É possível também alterar o IP do servidor da API em node, util caso a API execute em uma máquina e o IP
 acabe alerado do padrão que o app espera o 192.168.0.3
 
 
 ### Trocando o IP padrão
 
 - Na tela de login do aplicativo no canto superior direito, existe uma figura de engrenagem, 
 ao clicar nela, um tela se abre e é possível grava o IP da maquina que a API em node está sendo excutada
 - Aconcelha-se executar a aplicação em um emulador, tendo em vista que se executar em dispositivo físico,
 é necessario que a máquina da API esteja na mesma rede e ainda com as configurações de antivirus, firewall
 desabilitadas ou que permita o acesso remoto a maquina por parte do aplicativo. O que pode ser 
 demasiado custoso para um simples teste.
 
 ### Detalhes
 
 - A aplicação foi desenvolvida em android nativo, versão minima 16 e target 30
 - Faz uso da biblioteca Retrofit para as chamadas Rest das APIs
 - Versão 4.0 do Android Studio
 
 ### Executando
 
 - Baixar do projeto
 - Abrir com o projeto com o Android Studio
 - Aguardar ele baixar e indexar todas as depencias
 - Excutar o build de preferencia no emulado
 - Conferir o IP da maquina em que a API está executando
 - Alterar nas configurações do aplicativo como explicado anteriormente, o IP da serviço da API se for necessário
 - Executar um cadastro de uma conta(Ele vai logar automaticamente)
 - Sair pelo Botão, e logar novamente com a conta criada
 
 
 
 

