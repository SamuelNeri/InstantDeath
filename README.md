# InstantDeath

Plugin de Minecraft (Bukkit/Spigot) que remove a tela de morte e faz o respawn instantâneo do jogador.

## Descrição

O InstantDeath substitui o sistema de morte padrão do Minecraft por um respawn instantâneo. Quando o jogador recebe dano fatal, ao invés de ver a tela de morte tradicional, ele é imediatamente teletransportado para o ponto de respawn com a vida restaurada.

## Como Funciona

- Não tem o evento de dano que resultaria em morte
- Cancela a tela de morte padrão
- Restaura vida, fome e remove efeitos negativos
- Teleporta instantaneamente o jogador para seu ponto de respawn ou spawn do mundo

## Requisitos

- Minecraft 1.21+
- Spigot/Paper ou compatível
- Java 21

## Instalação

1. Baixe o arquivo `.jar` da seção [Releases](../../releases)
2. Coloque o arquivo na pasta `plugins` do seu servidor
3. Reinicie o servidor

## Uso

O plugin funciona automaticamente após a instalação. Não requer comandos ou configurações adicionais.

## Autor

**poledar**