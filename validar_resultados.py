"""Conferencia independente dos arquivos produzidos pelo programa Java."""
import csv
import itertools
import statistics
from collections import defaultdict
from decimal import Decimal
from pathlib import Path

pasta = Path(__file__).resolve().parent / "resultados"
with (pasta / "resultados_brutos.csv").open(encoding="utf-8", newline="") as arquivo:
    bruto = list(csv.DictReader(arquivo, delimiter=";"))
with (pasta / "resumo.csv").open(encoding="utf-8", newline="") as arquivo:
    resumo = list(csv.DictReader(arquivo, delimiter=";"))

tamanhos = (5000, 10000, 100000)
ordens = ("crescente", "decrescente", "aleatoria")
estruturas = ("array_linear", "array_ordenado", "lista_dupla", "hash", "bst", "avl", "rubro_negra")
testes = ("construcao", "busca_presente", "busca_ausente", "insercao", "remocao", "intervalo")
esperadas = set(itertools.product(tamanhos, ordens, estruturas, testes))

def chave(linha):
    return int(linha["tamanho"]), linha["ordem"], linha["estrutura"], linha["teste"]

grupos = defaultdict(list)
for linha in bruto:
    grupos[chave(linha)].append(linha)
assert len(bruto) == 1890, f"Numero de medicoes: {len(bruto)}"
assert set(grupos) == esperadas, "Combinacoes brutas faltantes ou extras"
assert len(resumo) == 378, f"Numero de resumos: {len(resumo)}"
assert {chave(linha) for linha in resumo} == esperadas, "Combinacoes de resumo"
for combinacao, linhas in grupos.items():
    n, ordem, estrutura, teste = combinacao
    assert sorted(int(linha["repeticao"]) for linha in linhas) == [1, 2, 3, 4, 5]
    operacoes = n if teste == "construcao" else 2000 if teste.startswith("busca") else 200 if teste == "intervalo" else n // 10
    assert all(int(linha["operacoes"]) == operacoes for linha in linhas)
    assert all(int(linha["tempo_total_ns"]) > 0 for linha in linhas)
for linha in resumo:
    dados = grupos[chave(linha)]
    mediana = Decimal(statistics.median(int(item["tempo_total_ns"]) for item in dados))
    assert abs(Decimal(linha["mediana_total_ms"]) - mediana / 1000000) <= Decimal("0.0000005")
    assert abs(Decimal(linha["mediana_por_operacao_ns"]) - mediana / int(dados[0]["operacoes"])) <= Decimal("0.0000005")

log = (pasta / "conferencia-execucao.txt").read_text(encoding="utf-8-sig")
assert log.count("conferencia=OK") == 378, "Conferencias de execucao"
assert "Concluido:" in log, "Execucao incompleta"
print("OK: 1.890 medicoes, 378 combinacoes, cinco repeticoes por combinacao.")
print("OK: tamanhos, ordens, estruturas, operacoes, medianas e conversoes de unidades.")
print("OK: 378 conferencias de execucao e encerramento normal.")
