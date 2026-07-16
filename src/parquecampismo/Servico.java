package parquecampismo;

import java.io.Serializable;

/** Servico adicional (Carro, Eletricidade ou Wifi) com preco por epoca. */
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    private final TipoServico tipo;
    private final double precoAlta;
    private final double precoBaixa;

    public Servico(TipoServico tipo, double precoAlta, double precoBaixa) {
        this.tipo = tipo;
        this.precoAlta = precoAlta;
        this.precoBaixa = precoBaixa;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public double getPreco(TipoEpoca epoca) {
        return epoca == TipoEpoca.ALTA ? precoAlta : precoBaixa;
    }

    @Override
    public String toString() {
        return tipo + " (" + precoAlta + "€ alta / " + precoBaixa + "€ baixa, por dia)";
    }
}
