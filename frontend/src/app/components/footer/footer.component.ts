import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { LinkedInBadgeComponent } from '../linked-in-badge/linked-in-badge.component';
import { AutocompleteFieldComponent } from "../form-fields-components/autocomplete-field/autocomplete-field.component";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  standalone: true, 
  imports: [CommonModule, LinkedInBadgeComponent, AutocompleteFieldComponent]
})
export class FooterComponent {

}
