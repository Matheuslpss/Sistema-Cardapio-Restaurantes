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

@Component({
  selector: 'app-root',
  imports: [FormsModule, DecimalPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  protected selectedCategory = '';
  protected modoAdmin = false;
  protected novaCategoria = '';
  protected categorias = signal<Categoria[]>([]);
  protected produtos = signal<Produto[]>([]);
  protected novoProduto = {
    nome: '',
    descricao: '',
    preco: 0,
    imagem: '',
    categoriaId: 0
  };
  protected categoriaEmEdicao: Categoria | null = null;
  protected nomeCategoriaEdicao = '';

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

  protected cadastrarProduto(): void {
    if (!this.novoProduto.nome.trim() || !this.novoProduto.categoriaId) {
      return;
    }

    const dados = {
      nome: this.novoProduto.nome.trim(),
      descricao: this.novoProduto.descricao.trim(),
      preco: Number(this.novoProduto.preco),
      imagem: this.novoProduto.imagem.trim(),
      categoria: { id: Number(this.novoProduto.categoriaId) }
    };

    this.http.post<Produto>('http://localhost:8080/produtos', dados).subscribe({
      next: (produto) => {
        this.produtos.update((produtos) => [...produtos, produto]);
        this.novoProduto = {
          nome: '',
          descricao: '',
          preco: 0,
          imagem: '',
          categoriaId: this.categorias()[0]?.id ?? 0
        };
        alert('Produto cadastrado com sucesso!');
      },
      error: (erro) => {
        console.error('Não foi possível cadastrar o produto', erro);
        alert('Não foi possível cadastrar o produto. Verifique o console do navegador para ver o erro.');
      }
    });
  }
}
