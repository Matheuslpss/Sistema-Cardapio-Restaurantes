import { Component, inject, signal } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

interface Categoria {
  id: number;
  nome: string;
}

interface Produto {
  id: number;
  nome: string;
  descricao: string;
  preco: number;
  imagem: string;
  categoria: Categoria;
}

interface ItemPedido {
  produto: Produto;
  quantidade: number;
}

@Component({
  selector: 'app-root',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected selectedCategory = '';
  protected modoAdmin = false;
  protected novaCategoria = '';
  protected categorias = signal<Categoria[]>([]);
  protected produtos = signal<Produto[]>([]);
  protected pedido = signal<ItemPedido[]>([]);
  protected novoProduto = {
    nome: '',
    descricao: '',
    preco: '',
    categoriaId: 0
  };
  protected imagemSelecionada: File | null = null;
  protected imagemSelecionadaPreview = '';
  protected categoriaEmEdicao: Categoria | null = null;
  protected nomeCategoriaEdicao = '';
  protected produtoEmEdicao: Produto | null = null;
  protected imagemEdicao: File | null = null;
  protected imagemEdicaoPreview = '';
  protected removerImagemEdicao = false;
  protected categoriaProdutoAberta: number | null = null;
  protected dadosProdutoEdicao = {
    nome: '',
    descricao: '',
    preco: 0,
    categoriaId: 0
  };

  private readonly http = inject(HttpClient);

  constructor() {
    this.carregarCategorias();
    this.carregarProdutos();
  }

  protected alternarModoAdmin(): void {
    this.modoAdmin = !this.modoAdmin;

    if (this.modoAdmin) {
      this.carregarCategorias();
    }
  }

  protected carregarCategorias(): void {
    this.http.get<Categoria[]>('http://localhost:8080/categorias').subscribe({
      next: (categorias) => {
        this.categorias.set(categorias);

        if (categorias.length > 0 && !categorias.some((categoria) => categoria.nome === this.selectedCategory)) {
          this.selectedCategory = categorias[0].nome;
        }

        if (this.novoProduto.categoriaId === 0 && categorias.length > 0) {
          this.novoProduto.categoriaId = categorias[0].id;
        }
      },
      error: (erro) => console.error('Não foi possível carregar as categorias', erro)
    });
  }

  protected carregarProdutos(): void {
    this.http.get<Produto[]>('http://localhost:8080/produtos').subscribe({
      next: (produtos) => this.produtos.set(produtos),
      error: (erro) => console.error('Não foi possível carregar os produtos', erro)
    });
  }

  protected produtosDaCategoria(categoriaId: number): Produto[] {
    return this.produtos().filter((produto) => produto.categoria?.id === categoriaId);
  }

  protected adicionarAoPedido(produto: Produto): void {
    this.pedido.update((itens) => {
      const itemExistente = itens.find((item) => item.produto.id === produto.id);

      if (itemExistente) {
        return itens.map((item) => item.produto.id === produto.id
          ? { ...item, quantidade: item.quantidade + 1 }
          : item);
      }

      return [...itens, { produto, quantidade: 1 }];
    });
  }

  protected diminuirQuantidade(produto: Produto): void {
    this.pedido.update((itens) => itens
      .map((item) => item.produto.id === produto.id
        ? { ...item, quantidade: item.quantidade - 1 }
        : item)
      .filter((item) => item.quantidade > 0));
  }

  protected removerDoPedido(produto: Produto): void {
    this.pedido.update((itens) => itens.filter((item) => item.produto.id !== produto.id));
  }

  protected quantidadePedido(): number {
    return this.pedido().reduce((total, item) => total + item.quantidade, 0);
  }

  protected totalPedido(): number {
    return this.pedido().reduce((total, item) => total + item.produto.preco * item.quantidade, 0);
  }

  protected imagemProduto(imagem: string): string {
    if (imagem.startsWith('http://') || imagem.startsWith('https://')) {
      return imagem;
    }

    return `http://localhost:8080${imagem.startsWith('/') ? imagem : `/${imagem}`}`;
  }

  protected selecionarImagem(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    this.imagemSelecionada = input.files?.[0] ?? null;
    this.imagemSelecionadaPreview = this.imagemSelecionada
      ? URL.createObjectURL(this.imagemSelecionada)
      : '';
  }

  protected formatarPreco(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    const numeros = input.value.replace(/\D/g, '');

    if (!numeros) {
      this.novoProduto.preco = '';
      input.value = '';
      return;
    }

    const valor = Number(numeros) / 100;
    const formatado = valor.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });

    this.novoProduto.preco = formatado;
    input.value = formatado;
    input.setSelectionRange(input.value.length, input.value.length);
  }

  protected cadastrarCategoria(): void {
    const nome = this.novaCategoria.trim();

    if (!nome) {
      return;
    }

    this.http.post<Categoria>('http://localhost:8080/categorias', { nome }).subscribe({
      next: (categoria) => {
        this.categorias.update((categorias) => [...categorias, categoria]);
        this.novaCategoria = '';
      },
      error: (erro) => console.error('Não foi possível cadastrar a categoria', erro)
    });
  }

  protected iniciarEdicao(categoria: Categoria): void {
    this.categoriaEmEdicao = categoria;
    this.nomeCategoriaEdicao = categoria.nome;
  }

  protected cancelarEdicao(): void {
    this.categoriaEmEdicao = null;
    this.nomeCategoriaEdicao = '';
  }

  protected salvarEdicaoCategoria(): void {
    const categoria = this.categoriaEmEdicao;
    const nome = this.nomeCategoriaEdicao.trim();

    if (!categoria || !nome) {
      return;
    }

    this.http.put<Categoria>(`http://localhost:8080/categorias/${categoria.id}`, { nome }).subscribe({
      next: (categoriaAtualizada) => {
        this.categorias.update((categorias) =>
          categorias.map((item) => item.id === categoriaAtualizada.id ? categoriaAtualizada : item)
        );

        if (this.selectedCategory === categoria.nome) {
          this.selectedCategory = categoriaAtualizada.nome;
        }

        this.cancelarEdicao();
      },
      error: (erro) => console.error('Não foi possível editar a categoria', erro)
    });
  }

  protected excluirCategoria(categoria: Categoria): void {
    if (!confirm(`Excluir a categoria "${categoria.nome}"?`)) {
      return;
    }

    this.http.delete(`http://localhost:8080/categorias/${categoria.id}`).subscribe({
      next: () => {
        this.categorias.update((categorias) => categorias.filter((item) => item.id !== categoria.id));

        if (this.selectedCategory === categoria.nome) {
          this.selectedCategory = '';
        }
      },
      error: (erro) => {
        console.error('Não foi possível excluir a categoria', erro);
        alert('Não foi possível excluir. Verifique se existem produtos nessa categoria.');
      }
    });
  }

  protected iniciarEdicaoProduto(produto: Produto): void {
    this.produtoEmEdicao = produto;
    this.imagemEdicao = null;
    this.imagemEdicaoPreview = produto.imagem ? this.imagemProduto(produto.imagem) : '';
    this.removerImagemEdicao = false;
    this.dadosProdutoEdicao = {
      nome: produto.nome,
      descricao: produto.descricao,
      preco: produto.preco,
      categoriaId: produto.categoria.id
    };
  }

  protected abrirCadastroProduto(categoria: Categoria): void {
    this.categoriaProdutoAberta = categoria.id;
    this.novoProduto.categoriaId = categoria.id;
  }

  protected cancelarCadastroProduto(): void {
    this.categoriaProdutoAberta = null;
    this.novoProduto = {
      nome: '',
      descricao: '',
      preco: '',
      categoriaId: 0
    };
    this.imagemSelecionada = null;
    this.imagemSelecionadaPreview = '';
  }

  protected cancelarEdicaoProduto(): void {
    this.produtoEmEdicao = null;
    this.imagemEdicao = null;
    this.imagemEdicaoPreview = '';
    this.removerImagemEdicao = false;
    this.dadosProdutoEdicao = { nome: '', descricao: '', preco: 0, categoriaId: 0 };
  }

  protected selecionarImagemEdicao(evento: Event): void {
    const input = evento.target as HTMLInputElement;
    this.imagemEdicao = input.files?.[0] ?? null;
    this.imagemEdicaoPreview = this.imagemEdicao
      ? URL.createObjectURL(this.imagemEdicao)
      : '';
    this.removerImagemEdicao = false;
  }

  protected precoFormatado(preco: number): string {
    return preco.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }

  protected salvarEdicaoProduto(): void {
    const produto = this.produtoEmEdicao;
    const nome = this.dadosProdutoEdicao.nome.trim();

    if (!produto || !nome || !this.dadosProdutoEdicao.categoriaId) {
      return;
    }

    const dados = new FormData();
    dados.append('nome', nome);
    dados.append('descricao', this.dadosProdutoEdicao.descricao.trim());
    dados.append('preco', String(Number(this.dadosProdutoEdicao.preco)));
    dados.append('categoriaId', String(Number(this.dadosProdutoEdicao.categoriaId)));
    dados.append('removerImagem', String(this.removerImagemEdicao));

    if (this.imagemEdicao) {
      dados.append('imagem', this.imagemEdicao);
    }

    this.http.put<Produto>(`http://localhost:8080/produtos/${produto.id}`, dados).subscribe({
      next: (produtoAtualizado) => {
        this.produtos.update((produtos) => produtos.map((item) =>
          item.id === produtoAtualizado.id ? produtoAtualizado : item
        ));
        this.cancelarEdicaoProduto();
      },
      error: (erro) => {
        console.error('Não foi possível editar o produto', erro);
        alert('Não foi possível editar o produto.');
      }
    });
  }

  protected excluirProduto(produto: Produto): void {
    if (!confirm(`Excluir o produto "${produto.nome}"?`)) {
      return;
    }

    this.http.delete(`http://localhost:8080/produtos/${produto.id}`).subscribe({
      next: () => {
        this.produtos.update((produtos) => produtos.filter((item) => item.id !== produto.id));
        this.pedido.update((itens) => itens.filter((item) => item.produto.id !== produto.id));
      },
      error: (erro) => {
        console.error('Não foi possível excluir o produto', erro);
        alert('Não foi possível excluir o produto.');
      }
    });
  }

  protected cadastrarProduto(): void {
    if (!this.novoProduto.nome.trim() || !this.novoProduto.categoriaId) {
      return;
    }

    const precoTexto = String(this.novoProduto.preco).trim().replace(',', '.');
    const preco = Number(precoTexto);

    if (!Number.isFinite(preco) || preco < 0) {
      alert('Digite um preço válido, por exemplo: 32,90.');
      return;
    }

    const dados = new FormData();
    dados.append('nome', this.novoProduto.nome.trim());
    dados.append('descricao', this.novoProduto.descricao.trim());
    dados.append('preco', String(preco));
    dados.append('categoriaId', String(Number(this.novoProduto.categoriaId)));

    if (this.imagemSelecionada) {
      dados.append('imagem', this.imagemSelecionada);
    }

    this.http.post<Produto>('http://localhost:8080/produtos', dados).subscribe({
      next: (produto) => {
        this.produtos.update((produtos) => [...produtos, produto]);
        this.novoProduto = {
          nome: '',
          descricao: '',
          preco: '',
          categoriaId: this.categorias()[0]?.id ?? 0
        };
        this.imagemSelecionada = null;
        this.imagemSelecionadaPreview = '';
        this.categoriaProdutoAberta = null;
        alert('Produto cadastrado com sucesso!');
      },
      error: (erro) => {
        console.error('Não foi possível cadastrar o produto', erro);
        alert('Não foi possível cadastrar o produto. Verifique o console do navegador para ver o erro.');
      }
    });
  }
}
