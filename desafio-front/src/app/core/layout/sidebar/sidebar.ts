import { Component } from '@angular/core';
import { SelectorButton } from '../../../shared/buttons/selector-button/selector-button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-sidebar',
  imports: [SelectorButton, MatIcon],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class Sidebar {
  dashboard = "Dashboard";
  gerenciarEmpresas = "Gerenciar Empresas";
  gerenciarFornecedores = "Gerenciar Fornecedores";

 showText(text: string){
  alert(text)
 }

  }


