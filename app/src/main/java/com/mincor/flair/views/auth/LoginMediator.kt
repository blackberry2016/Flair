package com.mincor.askme.mediators.login

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.mincor.flair.R
import com.mincor.flair.utils.Consts.FONT_SIZE_12
import com.mincor.flair.utils.Consts.FONT_SIZE_14
import com.mincor.flair.utils.Consts.ROUND_CORNERS_16
import com.mincor.flair.utils.dip8
import com.mincor.flair.views.auth.ForgotPassMediator
import com.mincor.flair.views.auth.RegistrationMediator
import com.rasalexman.flairframework.core.animation.LinearAnimator
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.interfaces.*
import com.rasalexman.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by Alex on 22.01.2017.
 */

class LoginMediator : Mediator() {

    override fun createLayout(context: Context): View = LoginUI().createView(AnkoContext.create(context, this))
    private val registerMediator: RegistrationMediator by mediatorLazy()
    private val forgotPassMediator: ForgotPassMediator by mediatorLazy()

    private var emailET:EditText? = null
    private var passwordET:EditText? = null

    internal fun singInUser() {
        log { "START LOGIN IN " }

        // Reset errors.
        emailET!!.error = null
        passwordET!!.error = null

        val emailStr = emailET!!.text.toString()
        val passStr = passwordET!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid emailET address.
        // There was an error; don't attempt login and focus the first
        // form field with an error.
        when {
            emailStr.isEmpty() -> {
                emailET!!.error = string(R.string.notSetEmailTF)
                focusView = emailET
                cancel = true
            }
            passStr.isEmpty() -> {       // Check for a valid password, if the user entered one.
                passwordET!!.error = string(R.string.notSetPassTF)
                focusView = passwordET
                cancel = true
            }
            passStr.length < 5 -> {  // Check for a valid password, if the user entered one.
                passwordET!!.error = string(R.string.minPassErrorTF) + " " + 5
                focusView = passwordET
                cancel = true
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            popToBack(LinearAnimator())
        }
    }


    private inner class LoginUI : AnkoComponent<LoginMediator> {
        override fun createView(ui: AnkoContext<LoginMediator>): View = with(ui) {
            verticalLayout {
                background = gradientBg(arrayOf(color(R.color.startColor), color(R.color.endColor)))
                gravity = Gravity.CENTER

                emailET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.emailTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }.lparams(wdthProc(0.6f))

                passwordET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.passTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.logInTF) {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    onClick {
                        singInUser()
                    }
                }.lparams(wdthProc(0.6f), hdthProc(0.06f)) {
                    topMargin = dip8()
                }

                button(R.string.registerTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        registerMediator.show(LinearAnimator())
                    }
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.forgotPassTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        forgotPassMediator.show(LinearAnimator())
                    }
                }.lparams(wdthProc(0.6f))

                button(R.string.backTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        popToBack(LinearAnimator())
                    }
                }.lparams(wdthProc(0.6f))
            }
        }
    }
}
