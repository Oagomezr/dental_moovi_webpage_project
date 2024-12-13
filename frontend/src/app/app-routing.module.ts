import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { CategoryProductsComponent } from './components/category-products/category-products.component';
import { ProductComponent } from './components/category-products/product/product.component';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';
import { EditPersonalInfoComponent } from './components/user-settings/edit-personal-info/edit-personal-info.component';
import { EditAddressesComponent } from './components/user-settings/edit-addresses/edit-addresses.component';
import { EditAddressComponent } from './components/user-settings/edit-addresses/edit-address/edit-address.component';
import { OrderDetailsComponent } from './components/order-details/order-details.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { ForgottenPwComponent } from './components/forgotten-pw/forgotten-pw.component';
import { ManageOrdersComponent } from './components/manage-orders/manage-orders.component';
import { OrderAdminComponent } from './components/order-admin/order-admin.component';
import { WhoAreWeComponent } from './components/who-are-we/who-are-we.component';
import { DataPolicyComponent } from './components/data-policy/data-policy.component';
import { TermsConditionsComponent } from './components/terms-conditions/terms-conditions.component';
import { tokenGuard } from './core/guards/token.guard';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignUpComponent },
  { path: 'pwForgotten', component: ForgottenPwComponent },
  { path: 'category/:parents', component: CategoryProductsComponent},
  { path: 'product/:product', component: ProductComponent},
  { path: 'settings', component: UserSettingsComponent },
  { path: 'settings/userInfo/:id', component: EditPersonalInfoComponent },
  { path: 'settings/addresses', component: EditAddressesComponent },
  { path: 'settings/addresses/:user/:address', component: EditAddressComponent },
  { path: 'orderDetails', component: OrderDetailsComponent },
  { path: 'orderAdmin', component: OrderAdminComponent, canActivate: [tokenGuard], data: { role: 'admin' }  },
  { path: 'manageOrders', component: ManageOrdersComponent },
  { path: 'whoAreWe', component: WhoAreWeComponent },
  { path: 'dataPolicy', component: DataPolicyComponent },
  { path: 'termsConditions', component: TermsConditionsComponent },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
