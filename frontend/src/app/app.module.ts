import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { CartBadgeComponent } from './components/header/cart-badge/cart-badge.component';
import { SelectorFieldComponent } from './components/form-fields-components/selector-field/selector-field.component';
import { PasswordFieldComponent } from './components/form-fields-components/password-field/password-field.component';
import { DateFieldComponent } from './components/form-fields-components/date-field/date-field.component';
import { CelPhoneFieldComponent } from './components/form-fields-components/cel-phone-field/cel-phone-field.component';
import { HoverBoxProfileComponent } from './components/header/hover-box-profile/hover-box-profile.component';
import { HoverBoxProductsComponent } from './components/header/hover-box-products/hover-box-products.component';
import { CategoryProductsComponent } from './components/category-products/category-products.component';
import { ProductsComponent } from './components/category-products/products/products.component';
import { ProductComponent } from './components/category-products/product/product.component';
import { DirectionComponent } from './components/category-products/direction/direction.component';
import { ConfirmCodeFieldComponent } from './components/form-fields-components/confirm-code-field/confirm-code-field.component';
import { DialogComponent } from './components/dialog/dialog.component';
import { VisibilityIconComponent } from './components/category-products/product/visibility-icon/visibility-icon.component';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import { EditPersonalInfoComponent } from './components/user-settings/edit-personal-info/edit-personal-info.component';
import { EditAddressesComponent } from './components/user-settings/edit-addresses/edit-addresses.component';
import { EditAddressComponent } from './components/user-settings/edit-addresses/edit-address/edit-address.component';
import { HoverBoxCartComponent } from './components/header/hover-box-cart/hover-box-cart.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { MatIconModule } from '@angular/material/icon';

@NgModule({ declarations: [
        AppComponent
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        CartBadgeComponent,
        SelectorFieldComponent,
        PasswordFieldComponent,
        DateFieldComponent,
        CelPhoneFieldComponent,
        VisibilityIconComponent,
        MatIconModule,
        CategoryProductsComponent,
        ProductsComponent,
        ProductComponent,
        DirectionComponent,
        ConfirmCodeFieldComponent,
        DialogComponent,
        HoverBoxCartComponent,
        HoverBoxProductsComponent,
        HoverBoxProfileComponent,
        HeaderComponent,
        HomeComponent,
        LoginComponent,
        SignUpComponent,
        PageNotFoundComponent,
        FooterComponent,
        UserSettingsComponent,
        EditPersonalInfoComponent,
        EditAddressesComponent,
        EditAddressComponent,
        OrderDetailsComponent], providers: [provideHttpClient(withInterceptorsFromDi())] })
export class AppModule { }
